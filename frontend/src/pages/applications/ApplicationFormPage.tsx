import { type FormEvent, useEffect, useState } from "react";
import { ArrowLeft, Save } from "lucide-react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createJobApplication,
  getJobApplicationById,
  updateJobApplication,
  type JobApplicationRequest,
} from "../../api/job-applications";
import type { ApplicationStatus } from "../../types/job-application";

type FormState = {
  companyName: string;
  jobTitle: string;
  location: string;
  contractType: string;
  status: ApplicationStatus;
  source: string;
  applicationUrl: string;
  notes: string;
  followUpAt: string;
};

const statusOptions: Array<{ label: string; value: ApplicationStatus }> = [
  { label: "Sauvegardée", value: "SAVED" },
  { label: "Envoyée", value: "APPLIED" },
  { label: "Entretien", value: "INTERVIEW" },
  { label: "Offre", value: "OFFER" },
  { label: "Refusée", value: "REJECTED" },
  { label: "Retirée", value: "WITHDRAWN" },
];

const initialForm: FormState = {
  companyName: "",
  jobTitle: "",
  location: "",
  contractType: "",
  status: "SAVED",
  source: "",
  applicationUrl: "",
  notes: "",
  followUpAt: "",
};

function ApplicationFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const isEditMode = Boolean(id);

  const [form, setForm] = useState<FormState>(initialForm);
  const [loading, setLoading] = useState(isEditMode);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadApplication() {
      if (!id) {
        return;
      }

      try {
        setError("");
        setLoading(true);

        const application = await getJobApplicationById(id);

        setForm({
          companyName: application.companyName,
          jobTitle: application.jobTitle,
          location: application.location ?? "",
          contractType: application.contractType ?? "",
          status: application.status,
          source: application.source ?? "",
          applicationUrl: application.applicationUrl ?? "",
          notes: application.notes ?? "",
          followUpAt: toDateTimeLocalValue(application.followUpAt),
        });
      } catch {
        setError("Impossible de charger la candidature.");
      } finally {
        setLoading(false);
      }
    }

    loadApplication();
  }, [id]);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const request: JobApplicationRequest = {
      companyName: form.companyName.trim(),
      jobTitle: form.jobTitle.trim(),
      location: emptyToNull(form.location),
      contractType: emptyToNull(form.contractType),
      status: form.status,
      source: emptyToNull(form.source),
      applicationUrl: emptyToNull(form.applicationUrl),
      notes: emptyToNull(form.notes),
      followUpAt: emptyToNull(form.followUpAt),
    };

    try {
      setError("");
      setSaving(true);

      if (id) {
        await updateJobApplication(id, request);
      } else {
        await createJobApplication(request);
      }

      navigate("/applications");
    } catch {
      setError("Impossible d’enregistrer la candidature.");
    } finally {
      setSaving(false);
    }
  }

  function updateField<Field extends keyof FormState>(
    field: Field,
    value: FormState[Field]
  ) {
    setForm((current) => ({
      ...current,
      [field]: value,
    }));
  }

  return (
    <section>
      <Link
        to="/applications"
        className="inline-flex items-center gap-2 text-sm text-slate-400 transition hover:text-white"
      >
        <ArrowLeft size={16} />
        Retour aux candidatures
      </Link>

      <div className="mt-6">
        <p className="text-sm font-medium text-blue-400">Candidatures</p>

        <h1 className="mt-2 text-3xl font-bold">
          {isEditMode ? "Modifier la candidature" : "Nouvelle candidature"}
        </h1>

        <p className="mt-3 max-w-2xl text-slate-400">
          Renseigne les informations principales de la candidature, son statut,
          sa source et une éventuelle date de relance.
        </p>
      </div>

      {loading && (
        <div className="mt-8 rounded-2xl border border-slate-800 bg-slate-900 p-6">
          <p className="text-slate-400">Chargement de la candidature...</p>
        </div>
      )}

      {!loading && (
        <form
          onSubmit={handleSubmit}
          className="mt-8 max-w-4xl rounded-2xl border border-slate-800 bg-slate-900 p-6"
        >
          <div className="grid gap-5 md:grid-cols-2">
            <div>
              <label className="mb-2 block text-sm text-slate-300">
                Entreprise *
              </label>
              <input
                className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
                value={form.companyName}
                onChange={(event) => updateField("companyName", event.target.value)}
                required
              />
            </div>

            <div>
              <label className="mb-2 block text-sm text-slate-300">
                Poste *
              </label>
              <input
                className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
                value={form.jobTitle}
                onChange={(event) => updateField("jobTitle", event.target.value)}
                required
              />
            </div>

            <div>
              <label className="mb-2 block text-sm text-slate-300">
                Localisation
              </label>
              <input
                className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
                value={form.location}
                onChange={(event) => updateField("location", event.target.value)}
                placeholder="Paris, Rennes, Remote..."
              />
            </div>

            <div>
              <label className="mb-2 block text-sm text-slate-300">
                Type de contrat
              </label>
              <input
                className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
                value={form.contractType}
                onChange={(event) => updateField("contractType", event.target.value)}
                placeholder="CDI, CDD, Alternance, Stage..."
              />
            </div>

            <div>
              <label className="mb-2 block text-sm text-slate-300">
                Statut
              </label>
              <select
                className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
                value={form.status}
                onChange={(event) =>
                  updateField("status", event.target.value as ApplicationStatus)
                }
              >
                {statusOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="mb-2 block text-sm text-slate-300">
                Source
              </label>
              <input
                className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
                value={form.source}
                onChange={(event) => updateField("source", event.target.value)}
                placeholder="LinkedIn, Indeed, Welcome to the Jungle..."
              />
            </div>

            <div>
              <label className="mb-2 block text-sm text-slate-300">
                URL de l’offre
              </label>
              <input
                className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
                type="url"
                value={form.applicationUrl}
                onChange={(event) => updateField("applicationUrl", event.target.value)}
                placeholder="https://..."
              />
            </div>

            <div>
              <label className="mb-2 block text-sm text-slate-300">
                Date de relance
              </label>
              <input
                className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
                type="datetime-local"
                value={form.followUpAt}
                onChange={(event) => updateField("followUpAt", event.target.value)}
              />
            </div>
          </div>

          <div className="mt-5">
            <label className="mb-2 block text-sm text-slate-300">
              Notes
            </label>
            <textarea
              className="min-h-32 w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
              value={form.notes}
              onChange={(event) => updateField("notes", event.target.value)}
              placeholder="Notes personnelles, contact, prochaine action..."
            />
          </div>

          {error && (
            <p className="mt-5 rounded-xl border border-red-900 bg-red-950 px-4 py-3 text-sm text-red-300">
              {error}
            </p>
          )}

          <div className="mt-6 flex flex-col gap-3 sm:flex-row sm:justify-end">
            <Link
              to="/applications"
              className="rounded-xl border border-slate-700 px-5 py-3 text-center text-sm text-slate-300 transition hover:border-slate-500 hover:text-white"
            >
              Annuler
            </Link>

            <button
              className="inline-flex items-center justify-center gap-2 rounded-xl bg-blue-600 px-5 py-3 text-sm font-semibold transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-60"
              type="submit"
              disabled={saving}
            >
              <Save size={16} />
              {saving ? "Enregistrement..." : "Enregistrer"}
            </button>
          </div>
        </form>
      )}
    </section>
  );
}

function emptyToNull(value: string) {
  const trimmedValue = value.trim();
  return trimmedValue.length > 0 ? trimmedValue : null;
}

function toDateTimeLocalValue(value: string | null) {
  if (!value) {
    return "";
  }

  return value.slice(0, 16);
}

export default ApplicationFormPage;