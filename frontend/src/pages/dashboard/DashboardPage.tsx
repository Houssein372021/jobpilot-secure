import { useEffect, useState } from "react";
import {
  AlertTriangle,
  BellRing,
  BriefcaseBusiness,
  CalendarCheck,
  CalendarClock,
  ClipboardList,
  Clock3,
  Heart,
  MapPin,
  Send,
  Trophy,
  XCircle,
} from "lucide-react";
import {
  getDashboardActionSummary,
  getDashboardStats,
  getOverdueFollowUps,
  getTodayFollowUps,
  getUpcomingFollowUps,
} from "../../api/dashboard";
import type { DashboardActionSummary, DashboardStats } from "../../types/dashboard";
import type { JobApplication } from "../../types/job-application";

function DashboardPage() {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [actionSummary, setActionSummary] = useState<DashboardActionSummary | null>(null);
  const [todayFollowUps, setTodayFollowUps] = useState<JobApplication[]>([]);
  const [overdueFollowUps, setOverdueFollowUps] = useState<JobApplication[]>([]);
  const [upcomingFollowUps, setUpcomingFollowUps] = useState<JobApplication[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadDashboard() {
      try {
        setError("");

        const [
          statsResponse,
          actionSummaryResponse,
          todayFollowUpsResponse,
          overdueFollowUpsResponse,
          upcomingFollowUpsResponse,
        ] = await Promise.all([
          getDashboardStats(),
          getDashboardActionSummary(),
          getTodayFollowUps(),
          getOverdueFollowUps(),
          getUpcomingFollowUps(),
        ]);

        setStats(statsResponse);
        setActionSummary(actionSummaryResponse);
        setTodayFollowUps(todayFollowUpsResponse);
        setOverdueFollowUps(overdueFollowUpsResponse);
        setUpcomingFollowUps(upcomingFollowUpsResponse);
      } catch {
        setError("Impossible de charger les données du dashboard.");
      } finally {
        setLoading(false);
      }
    }

    loadDashboard();
  }, []);

  const statsCards = stats
    ? [
        {
          label: "Total candidatures",
          value: stats.total,
          icon: BriefcaseBusiness,
        },
        {
          label: "Sauvegardées",
          value: stats.saved,
          icon: CalendarCheck,
        },
        {
          label: "Envoyées",
          value: stats.applied,
          icon: Send,
        },
        {
          label: "Entretiens",
          value: stats.interview,
          icon: CalendarCheck,
        },
        {
          label: "Offres",
          value: stats.offer,
          icon: Trophy,
        },
        {
          label: "Refusées",
          value: stats.rejected,
          icon: XCircle,
        },
        {
          label: "Retirées",
          value: stats.withdrawn,
          icon: XCircle,
        },
        {
          label: "Favorites",
          value: stats.favorites,
          icon: Heart,
        },
      ]
    : [];

  const actionCards = actionSummary
    ? [
        {
          label: "Relances aujourd’hui",
          value: actionSummary.todayFollowUps,
          icon: BellRing,
        },
        {
          label: "Relances en retard",
          value: actionSummary.overdueFollowUps,
          icon: AlertTriangle,
        },
        {
          label: "Relances à venir",
          value: actionSummary.upcomingFollowUps,
          icon: CalendarClock,
        },
        {
          label: "Sans relance prévue",
          value: actionSummary.applicationsWithoutFollowUp,
          icon: ClipboardList,
        },
        {
          label: "Candidatures sauvegardées",
          value: actionSummary.savedApplications,
          icon: BriefcaseBusiness,
        },
        {
          label: "Entretiens en cours",
          value: actionSummary.interviewApplications,
          icon: CalendarCheck,
        },
      ]
    : [];

  return (
    <section>
      <div>
        <p className="text-sm font-medium text-blue-400">Tableau de bord</p>

        <h1 className="mt-2 text-3xl font-bold">Dashboard</h1>

        <p className="mt-3 max-w-2xl text-slate-400">
          Visualise rapidement l’état de tes candidatures, tes relances et les
          actions importantes à traiter.
        </p>
      </div>

      {loading && (
        <div className="mt-8 rounded-2xl border border-slate-800 bg-slate-900 p-6">
          <p className="text-slate-400">Chargement du dashboard...</p>
        </div>
      )}

      {error && (
        <div className="mt-8 rounded-2xl border border-red-900 bg-red-950 p-6">
          <p className="text-red-300">{error}</p>
        </div>
      )}

      {!loading && !error && stats && actionSummary && (
        <>
          <div className="mt-8">
            <h2 className="text-xl font-semibold">Statistiques</h2>

            <div className="mt-4 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
              {statsCards.map((card) => {
                const Icon = card.icon;

                return (
                  <div
                    key={card.label}
                    className="rounded-2xl border border-slate-800 bg-slate-900 p-5 shadow-sm"
                  >
                    <div className="flex items-center justify-between gap-4">
                      <div>
                        <p className="text-sm text-slate-400">{card.label}</p>
                        <p className="mt-2 text-3xl font-bold">{card.value}</p>
                      </div>

                      <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-slate-800 text-blue-400">
                        <Icon size={22} />
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          <div className="mt-10">
            <h2 className="text-xl font-semibold">Actions à traiter</h2>

            <div className="mt-4 grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
              {actionCards.map((card) => {
                const Icon = card.icon;

                return (
                  <div
                    key={card.label}
                    className="rounded-2xl border border-slate-800 bg-slate-900 p-5 shadow-sm"
                  >
                    <div className="flex items-center justify-between gap-4">
                      <div>
                        <p className="text-sm text-slate-400">{card.label}</p>
                        <p className="mt-2 text-3xl font-bold">{card.value}</p>
                      </div>

                      <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-slate-800 text-blue-400">
                        <Icon size={22} />
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          <div className="mt-10">
            <h2 className="text-xl font-semibold">Relances</h2>

            <div className="mt-4 grid gap-4 xl:grid-cols-3">
              <FollowUpList
                title="Aujourd’hui"
                description="Les candidatures à relancer aujourd’hui."
                applications={todayFollowUps}
                emptyMessage="Aucune relance prévue aujourd’hui."
              />

              <FollowUpList
                title="En retard"
                description="Les relances dépassées à traiter en priorité."
                applications={overdueFollowUps}
                emptyMessage="Aucune relance en retard."
              />

              <FollowUpList
                title="À venir"
                description="Les prochaines relances planifiées."
                applications={upcomingFollowUps}
                emptyMessage="Aucune relance à venir."
              />
            </div>
          </div>
        </>
      )}
    </section>
  );
}

type FollowUpListProps = {
  title: string;
  description: string;
  applications: JobApplication[];
  emptyMessage: string;
};

function FollowUpList({
  title,
  description,
  applications,
  emptyMessage,
}: FollowUpListProps) {
  return (
    <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5">
      <div>
        <h3 className="font-semibold">{title}</h3>
        <p className="mt-1 text-sm text-slate-400">{description}</p>
      </div>

      {applications.length === 0 && (
        <p className="mt-6 rounded-xl border border-slate-800 bg-slate-950 p-4 text-sm text-slate-400">
          {emptyMessage}
        </p>
      )}

      {applications.length > 0 && (
        <div className="mt-6 space-y-3">
          {applications.map((application) => (
            <article
              key={application.id}
              className="rounded-xl border border-slate-800 bg-slate-950 p-4"
            >
              <div className="flex items-start justify-between gap-3">
                <div>
                  <h4 className="font-semibold">{application.companyName}</h4>
                  <p className="mt-1 text-sm text-slate-400">
                    {application.jobTitle}
                  </p>
                </div>

                <span className="rounded-full border border-slate-700 px-3 py-1 text-xs text-slate-300">
                  {getStatusLabel(application.status)}
                </span>
              </div>

              <div className="mt-4 space-y-2 text-sm text-slate-400">
                {application.followUpAt && (
                  <p className="flex items-center gap-2">
                    <Clock3 size={15} />
                    {formatDateTime(application.followUpAt)}
                  </p>
                )}

                {application.location && (
                  <p className="flex items-center gap-2">
                    <MapPin size={15} />
                    {application.location}
                  </p>
                )}
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
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

export default DashboardPage;