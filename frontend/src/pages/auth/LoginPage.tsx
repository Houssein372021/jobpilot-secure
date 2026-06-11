import { type FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { login } from "../../api/auth";
import { saveAuthResponse } from "../../utils/auth-storage";

function LoginPage() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("houssein@test.com");
  const [password, setPassword] = useState("SecurePassword123!");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await login({
        email,
        password,
      });

      saveAuthResponse(response);

      navigate("/dashboard");
    } catch {
      setError("Email ou mot de passe incorrect.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="flex min-h-screen items-center justify-center bg-slate-950 px-6 text-white">
      <section className="w-full max-w-md rounded-2xl border border-slate-800 bg-slate-900 p-8 shadow-xl">
        <p className="mb-2 text-sm font-medium text-blue-400">JobPilot Secure</p>

        <h1 className="text-3xl font-bold">Connexion</h1>

        <p className="mt-2 text-sm text-slate-400">
          Connecte-toi pour gérer tes candidatures, relances et statistiques.
        </p>

        <form onSubmit={handleSubmit} className="mt-8 space-y-5">
          <div>
            <label className="mb-2 block text-sm text-slate-300">
              Email
            </label>
            <input
              className="w-full rounded-lg border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              required
            />
          </div>

          <div>
            <label className="mb-2 block text-sm text-slate-300">
              Mot de passe
            </label>
            <input
              className="w-full rounded-lg border border-slate-700 bg-slate-950 px-4 py-3 text-sm outline-none transition focus:border-blue-500"
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
            />
          </div>

          {error && (
            <p className="rounded-lg border border-red-900 bg-red-950 px-4 py-3 text-sm text-red-300">
              {error}
            </p>
          )}

          <button
            className="w-full rounded-lg bg-blue-600 px-4 py-3 text-sm font-semibold transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-60"
            type="submit"
            disabled={loading}
          >
            {loading ? "Connexion..." : "Se connecter"}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-slate-400">
          Pas encore de compte ?{" "}
          <Link className="font-medium text-blue-400 hover:text-blue-300" to="/register">
            Créer un compte
          </Link>
        </p>
      </section>
    </main>
  );
}

export default LoginPage;