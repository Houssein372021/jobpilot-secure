import { BriefcaseBusiness, LayoutDashboard, LogOut, ShieldCheck } from "lucide-react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { clearAuthStorage, getStoredUser } from "../utils/auth-storage";


function MainLayout() {
  const navigate = useNavigate();
  const user = getStoredUser();

  function handleLogout() {
    clearAuthStorage();
    navigate("/login");
  }

  const linkClassName = ({ isActive }: { isActive: boolean }) =>
    [
      "flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium transition",
      isActive
        ? "bg-blue-600 text-white"
        : "text-slate-300 hover:bg-slate-800 hover:text-white",
    ].join(" ");

  return (
    <div className="min-h-screen bg-slate-950 text-white">
      <aside className="fixed inset-y-0 left-0 hidden w-72 border-r border-slate-800 bg-slate-900 p-6 lg:block">
        <div className="flex items-center gap-3">
          <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-blue-600">
            <ShieldCheck size={24} />
          </div>

          <div>
            <p className="text-lg font-bold">JobPilot</p>
            <p className="text-xs text-slate-400">Secure dashboard</p>
          </div>
        </div>

        <nav className="mt-10 space-y-2">
          <NavLink to="/dashboard" className={linkClassName}>
            <LayoutDashboard size={18} />
            Dashboard
          </NavLink>

          <NavLink to="/applications" className={linkClassName}>
            <BriefcaseBusiness size={18} />
            Candidatures
          </NavLink>
        </nav>
      </aside>

      <div className="lg:pl-72">
        <header className="sticky top-0 z-10 border-b border-slate-800 bg-slate-950/90 px-6 py-4 backdrop-blur">
          <div className="flex items-center justify-between gap-4">
            <div>
              <p className="text-sm text-slate-400">Bienvenue</p>
              <h1 className="text-lg font-semibold">
                {user ? `${user.firstName} ${user.lastName}` : "Utilisateur"}
              </h1>
            </div>

            <button
              onClick={handleLogout}
              className="flex items-center gap-2 rounded-xl border border-slate-700 px-4 py-2 text-sm text-slate-300 transition hover:border-red-500 hover:text-red-300"
              type="button"
            >
              <LogOut size={16} />
              Déconnexion
            </button>
          </div>
        </header>

        <main className="px-6 py-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default MainLayout;