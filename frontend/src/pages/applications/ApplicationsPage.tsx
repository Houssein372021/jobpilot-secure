import { useEffect, useState } from "react";
import {
  BriefcaseBusiness,
  CalendarClock,
  ExternalLink,
  Heart,
  MapPin,
} from "lucide-react";
import { getJobApplications } from "../../api/job-applications";
import type { JobApplication } from "../../types/job-application";
import type { PageResponse } from "../../types/pagination";

function ApplicationsPage() {
  const [applicationsPage, setApplicationsPage] =
    useState<PageResponse<JobApplication> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadApplications() {
      try {
        setError("");
        const response = await getJobApplications();
        setApplicationsPage(response);
      } catch {
        setError("Impossible de charger les candidatures.");
      } finally {
        setLoading(false);
      }
    }

    loadApplications();
  }, []);

  const applications = applicationsPage?.content ?? [];

  return (
    <section>
      <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-start">
        <div>
          <p className="text-sm font-medium text-blue-400">Candidatures</p>

          <h1 className="mt-2 text-3xl font-bold">Mes candidatures</h1>

          <p className="mt-3 max-w-2xl text-slate-400">
            Consulte la liste de tes candidatures avec leur statut, leur date de
            relance, leur entreprise et leur source.
          </p>
        </div>

        {applicationsPage && (
          <div className="rounded-2xl border border-slate-800 bg-slate-900 px-5 py-4">
            <p className="text-sm text-slate-400">Total</p>
            <p className="mt-1 text-2xl font-bold">
              {applicationsPage.totalElements}
            </p>
          </div>
        )}
      </div>

      {loading && (
        <div className="mt-8 rounded-2xl border border-slate-800 bg-slate-900 p-6">
          <p className="text-slate-400">Chargement des candidatures...</p>
        </div>
      )}

      {error && (
        <div className="mt-8 rounded-2xl border border-red-900 bg-red-950 p-6">
          <p className="text-red-300">{error}</p>
        </div>
      )}

      {!loading && !error && applications.length === 0 && (
        <div className="mt-8 rounded-2xl border border-slate-800 bg-slate-900 p-8 text-center">
          <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-2xl bg-slate-800 text-blue-400">
            <BriefcaseBusiness size={24} />
          </div>

          <h2 className="mt-4 text-xl font-semibold">
            Aucune candidature pour le moment
          </h2>

          <p className="mt-2 text-sm text-slate-400">
            Les candidatures créées depuis l’API apparaîtront ici.
          </p>
        </div>
      )}

      {!loading && !error && applications.length > 0 && (
        <div className="mt-8 overflow-hidden rounded-2xl border border-slate-800 bg-slate-900">
          <div className="hidden grid-cols-[1.4fr_1.2fr_0.8fr_1fr_0.8fr] gap-4 border-b border-slate-800 px-5 py-4 text-sm font-medium text-slate-400 lg:grid">
            <span>Entreprise</span>
            <span>Poste</span>
            <span>Statut</span>
            <span>Relance</span>
            <span>Source</span>
          </div>

          <div className="divide-y divide-slate-800">
            {applications.map((application) => (
              <article
                key={application.id}
                className="grid gap-4 px-5 py-5 lg:grid-cols-[1.4fr_1.2fr_0.8fr_1fr_0.8fr] lg:items-center"
              >
                <div>
                  <div className="flex items-center gap-2">
                    <h2 className="font-semibold">
                      {application.companyName}
                    </h2>

                    {application.favorite && (
                      <Heart
                        size={16}
                        className="fill-red-500 text-red-500"
                      />
                    )}
                  </div>

                  {application.location && (
                    <p className="mt-2 flex items-center gap-2 text-sm text-slate-400">
                      <MapPin size={15} />
                      {application.location}
                    </p>
                  )}
                </div>

                <div>
                  <p className="font-medium lg:font-normal">
                    {application.jobTitle}
                  </p>

                  {application.contractType && (
                    <p className="mt-2 text-sm text-slate-400">
                      {application.contractType}
                    </p>
                  )}
                </div>

                <div>
                  <span className="inline-flex rounded-full border border-slate-700 px-3 py-1 text-xs text-slate-300">
                    {getStatusLabel(application.status)}
                  </span>
                </div>

                <div>
                  {application.followUpAt ? (
                    <p className="flex items-center gap-2 text-sm text-slate-300">
                      <CalendarClock size={15} />
                      {formatDateTime(application.followUpAt)}
                    </p>
                  ) : (
                    <p className="text-sm text-slate-500">Non planifiée</p>
                  )}
                </div>

                <div className="flex items-center gap-3">
                  <p className="text-sm text-slate-400">
                    {application.source ?? "Non précisée"}
                  </p>

                  {application.applicationUrl && (
                    <a
                      href={application.applicationUrl}
                      target="_blank"
                      rel="noreferrer"
                      className="text-blue-400 transition hover:text-blue-300"
                      title="Ouvrir l’offre"
                    >
                      <ExternalLink size={16} />
                    </a>
                  )}
                </div>
              </article>
            ))}
          </div>
        </div>
      )}

      {applicationsPage && applicationsPage.totalPages > 1 && (
        <div className="mt-5 rounded-2xl border border-slate-800 bg-slate-900 p-4 text-sm text-slate-400">
          Page {applicationsPage.page + 1} sur {applicationsPage.totalPages}
        </div>
      )}
    </section>
  );
}

function formatDateTime(value: string) {
  return new Intl.DateTimeFormat("fr-FR", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(value));
}

function getStatusLabel(status: JobApplication["status"]) {
  const labels: Record<JobApplication["status"], string> = {
    SAVED: "Sauvegardée",
    APPLIED: "Envoyée",
    INTERVIEW: "Entretien",
    OFFER: "Offre",
    REJECTED: "Refusée",
    WITHDRAWN: "Retirée",
  };

  return labels[status];
}

export default ApplicationsPage;