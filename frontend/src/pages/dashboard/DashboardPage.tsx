function DashboardPage() {
  return (
    <section>
      <div>
        <p className="text-sm font-medium text-blue-400">Tableau de bord</p>

        <h1 className="mt-2 text-3xl font-bold">
          Dashboard
        </h1>

        <p className="mt-3 max-w-2xl text-slate-400">
          Ici, tu verras bientôt les statistiques, les relances du jour,
          les relances en retard et le résumé des actions importantes.
        </p>
      </div>

      <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5">
          <p className="text-sm text-slate-400">Statistiques</p>
          <p className="mt-2 text-2xl font-bold">À venir</p>
        </div>

        <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5">
          <p className="text-sm text-slate-400">Relances</p>
          <p className="mt-2 text-2xl font-bold">À venir</p>
        </div>

        <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5">
          <p className="text-sm text-slate-400">Candidatures</p>
          <p className="mt-2 text-2xl font-bold">À venir</p>
        </div>

        <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5">
          <p className="text-sm text-slate-400">Actions</p>
          <p className="mt-2 text-2xl font-bold">À venir</p>
        </div>
      </div>
    </section>
  );
}

export default DashboardPage;