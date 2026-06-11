import { type FormEvent, useEffect, useState } from "react";
import {
  BriefcaseBusiness,
  CalendarClock,
  ChevronLeft,
  ChevronRight,
  ExternalLink,
  Heart,
  MapPin,
  Search,
  Pencil,
  Plus,
} from "lucide-react";
import {
  getJobApplications,
  type FavoriteFilter,
  type JobApplicationsSortBy,
  type SortDirection,
} from "../../api/job-applications";
import type { ApplicationStatus, JobApplication } from "../../types/job-application";
import type { PageResponse } from "../../types/pagination";
import { Link } from "react-router-dom";

const PAGE_SIZE = 10;

const statusOptions: Array<{ label: string; value: ApplicationStatus | "ALL" }> = [
  { label: "Tous les statuts", value: "ALL" },
  { label: "Sauvegardée", value: "SAVED" },
  { label: "Envoyée", value: "APPLIED" },
  { label: "Entretien", value: "INTERVIEW" },
  { label: "Offre", value: "OFFER" },
  { label: "Refusée", value: "REJECTED" },
  { label: "Retirée", value: "WITHDRAWN" },
];

const favoriteOptions: Array<{ label: string; value: FavoriteFilter }> = [
  { label: "Toutes", value: "all" },
  { label: "Favorites", value: "true" },
  { label: "Non favorites", value: "false" },
];

const sortOptions: Array<{ label: string; value: JobApplicationsSortBy }> = [
  { label: "Date de création", value: "createdAt" },
  { label: "Dernière modification", value: "updatedAt" },
  { label: "Entreprise", value: "companyName" },
  { label: "Poste", value: "jobTitle" },
  { label: "Statut", value: "status" },
  { label: "Favori", value: "favorite" },
];

function ApplicationsPage() {
  const [applicationsPage, setApplicationsPage] =
    useState<PageResponse<JobApplication> | null>(null);

  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [search, setSearch] = useState("");
  const [status, setStatus] = useState<ApplicationStatus | "ALL">("ALL");
  const [favorite, setFavorite] = useState<FavoriteFilter>("all");
  const [sortBy, setSortBy] = useState<JobApplicationsSortBy>("createdAt");
  const [direction, setDirection] = useState<SortDirection>("desc");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadApplications() {
      try {
        setLoading(true);
        setError("");

        const response = await getJobApplications({
          page,
          size: PAGE_SIZE,
          search,
          status,
          favorite,
          sortBy,
          direction,
        });

        setApplicationsPage(response);
      } catch {
        setError("Impossible de charger les candidatures.");
      } finally {
        setLoading(false);
      }
    }

    loadApplications();
  }, [page, search, status, favorite, sortBy, direction]);

  function handleSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setPage(0);
    setSearch(searchInput);
  }

  function handleResetFilters() {
    setPage(0);
    setSearchInput("");
    setSearch("");
    setStatus("ALL");
    setFavorite("all");
    setSortBy("createdAt");
    setDirection("desc");
  }

  const applications = applicationsPage?.content ?? [];
  const currentPage = applicationsPage ? applicationsPage.page + 1 : 1;
  const totalPages = applicationsPage?.totalPages ?? 1;

  return (
    <section>
      <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-start">
        <div>
          <p className="text-sm font-medium text-blue-400">Candidatures</p>

          <h1 className="mt-2 text-3xl font-bold">Mes candidatures</h1>

          <p className="mt-3 max-w-2xl text-slate-400">
            Consulte, recherche, filtre et trie tes candidatures depuis une seule page.
          </p>
        </div>

        {applicationsPage && (
          <div className="rounded-2xl border border-slate-800 bg-slate-900 px-5 py-4">
            <p className="text-sm text-slate-400">Total</p>
            <p className="mt-1 text-2xl font-bold">
              {applicationsPage.totalElements}
            </p>
            <Link
              to="/applications/new"
              className="inline-flex items-center justify-center gap-2 rounded-xl bg-blue-600 px-5 py-3 text-sm font-semibold transition hover:bg-blue-500"
            >
              <Plus size={16} />
              Nouvelle candidature
            </Link>
          </div>
        )}
      </div>

      <div className="mt-8 rounded-2xl border border-slate-800 bg-slate-900 p-5">
        <form onSubmit={handleSearch} className="grid gap-4 xl:grid-cols-[1.4fr_1fr_1fr_1fr_1fr_auto]">
          <div>
            <label className="mb-2 block text-sm text-slate-300">
              Recherche
            </label>

            <div className="flex items-center gap-2 rounded-xl border border-slate-700 bg-slate-950 px-4 py-3">
              <Search size={17} className="text-slate-500" />

              <input
                className="w-full bg-transparent text-sm outline-none placeholder:text-slate-600"
                placeholder="Entreprise, poste, ville, source..."
                value={searchInput}
                onChange={(event) => setSearchInput(event.target.value)}
              />
            </div>
          </div>

          <div>
            <label className="mb-2 block text-sm text-slate-300">
              Statut
            </label>

            <select
              className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none"
              value={status}
              onChange={(event) => {
                setPage(0);
                setStatus(event.target.value as ApplicationStatus | "ALL");
              }}
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
              Favoris
            </label>

            <select
              className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none"
              value={favorite}
              onChange={(event) => {
                setPage(0);
                setFavorite(event.target.value as FavoriteFilter);
              }}
            >
              {favoriteOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-2 block text-sm text-slate-300">
              Trier par
            </label>

            <select
              className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none"
              value={sortBy}
              onChange={(event) => {
                setPage(0);
                setSortBy(event.target.value as JobApplicationsSortBy);
              }}
            >
              {sortOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-2 block text-sm text-slate-300">
              Ordre
            </label>

            <select
              className="w-full rounded-xl border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none"
              value={direction}
              onChange={(event) => {
                setPage(0);
                setDirection(event.target.value as SortDirection);
              }}
            >
              <option value="desc">Descendant</option>
              <option value="asc">Ascendant</option>
            </select>
          </div>

          <div className="flex items-end gap-2">
            <button
              className="rounded-xl bg-blue-600 px-4 py-3 text-sm font-semibold transition hover:bg-blue-500"
              type="submit"
            >
              Rechercher
            </button>

            <button
              className="rounded-xl border border-slate-700 px-4 py-3 text-sm text-slate-300 transition hover:border-slate-500 hover:text-white"
              type="button"
              onClick={handleResetFilters}
            >
              Reset
            </button>
          </div>
        </form>
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
            Aucune candidature trouvée
          </h2>

          <p className="mt-2 text-sm text-slate-400">
            Essaie de modifier la recherche ou les filtres.
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
            <span>Actions</span>
          </div>

          <div className="divide-y divide-slate-800">
            {applications.map((application) => (
              <article
                key={application.id}
                className="grid gap-4 px-5 py-5 lg:grid-cols-[1.3fr_1.1fr_0.8fr_1fr_0.8fr_auto] lg:items-center"
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
                  <Link
                    to={`/applications/${application.id}/edit`}
                    className="inline-flex items-center gap-2 rounded-xl border border-slate-700 px-3 py-2 text-sm text-slate-300 transition hover:border-blue-500 hover:text-blue-300"
                  >
                    <Pencil size={15} />
                    Modifier
                  </Link>

              </article>
            ))}
          </div>
        </div>
      )}

      {applicationsPage && (
        <div className="mt-5 flex flex-col justify-between gap-3 rounded-2xl border border-slate-800 bg-slate-900 p-4 text-sm text-slate-400 sm:flex-row sm:items-center">
          <p>
            Page {currentPage} sur {totalPages || 1}
          </p>

          <div className="flex gap-2">
            <button
              className="flex items-center gap-2 rounded-xl border border-slate-700 px-4 py-2 transition hover:border-slate-500 hover:text-white disabled:cursor-not-allowed disabled:opacity-40"
              type="button"
              disabled={applicationsPage.page === 0}
              onClick={() => setPage((current) => Math.max(current - 1, 0))}
            >
              <ChevronLeft size={16} />
              Précédent
            </button>

            <button
              className="flex items-center gap-2 rounded-xl border border-slate-700 px-4 py-2 transition hover:border-slate-500 hover:text-white disabled:cursor-not-allowed disabled:opacity-40"
              type="button"
              disabled={applicationsPage.page + 1 >= applicationsPage.totalPages}
              onClick={() => setPage((current) => current + 1)}
            >
              Suivant
              <ChevronRight size={16} />
            </button>
          </div>
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