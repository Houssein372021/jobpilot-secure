function DashboardPage() {
  const user = localStorage.getItem("user");
  const parsedUser = user ? JSON.parse(user) : null;

  return (
    <main className="min-h-screen bg-slate-950 px-6 py-10 text-white">
      <section className="mx-auto max-w-5xl">
        <p className="text-sm font-medium text-blue-400">JobPilot Secure</p>

        <h1 className="mt-2 text-3xl font-bold">
          Dashboard
        </h1>

        <p className="mt-3 text-slate-400">
          Connexion réussie. Le dashboard complet sera ajouté dans les prochaines étapes.
        </p>

        {parsedUser && (
          <div className="mt-8 rounded-2xl border border-slate-800 bg-slate-900 p-6">
            <p className="text-sm text-slate-400">Utilisateur connecté</p>
            <p className="mt-2 font-semibold">
              {parsedUser.firstName} {parsedUser.lastName}
            </p>
            <p className="text-sm text-slate-400">{parsedUser.email}</p>
          </div>
        )}
      </section>
    </main>
  );
}

export default DashboardPage;