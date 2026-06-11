function ApplicationsPage() {
  return (
    <section>
      <div>
        <p className="text-sm font-medium text-blue-400">Candidatures</p>

        <h1 className="mt-2 text-3xl font-bold">
          Mes candidatures
        </h1>

        <p className="mt-3 max-w-2xl text-slate-400">
          La liste complète des candidatures sera ajoutée dans les prochaines étapes.
        </p>
      </div>

      <div className="mt-8 rounded-2xl border border-slate-800 bg-slate-900 p-6">
        <p className="text-slate-300">
          Page prête pour afficher la recherche, les filtres, la pagination,
          le tri et les actions rapides.
        </p>
      </div>
    </section>
  );
}

export default ApplicationsPage;