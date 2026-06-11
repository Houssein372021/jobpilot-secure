import { useEffect, useState } from "react";
import { BriefcaseBusiness, CalendarCheck, Heart, Send, Trophy, XCircle } from "lucide-react";
import { getDashboardStats } from "../../api/dashboard";
import type { DashboardStats } from "../../types/dashboard";

function DashboardPage() {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadStats() {
      try {
        setError("");
        const response = await getDashboardStats();
        setStats(response);
      } catch {
        setError("Impossible de charger les statistiques du dashboard.");
      } finally {
        setLoading(false);
      }
    }

    loadStats();
  }, []);

  const cards = stats
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

  return (
    <section>
      <div>
        <p className="text-sm font-medium text-blue-400">Tableau de bord</p>

        <h1 className="mt-2 text-3xl font-bold">
          Dashboard
        </h1>

        <p className="mt-3 max-w-2xl text-slate-400">
          Visualise rapidement l’état de tes candidatures, tes entretiens,
          tes offres, tes refus et tes favoris.
        </p>
      </div>

      {loading && (
        <div className="mt-8 rounded-2xl border border-slate-800 bg-slate-900 p-6">
          <p className="text-slate-400">Chargement des statistiques...</p>
        </div>
      )}

      {error && (
        <div className="mt-8 rounded-2xl border border-red-900 bg-red-950 p-6">
          <p className="text-red-300">{error}</p>
        </div>
      )}

      {!loading && !error && stats && (
        <div className="mt-8 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {cards.map((card) => {
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
      )}
    </section>
  );
}

export default DashboardPage;