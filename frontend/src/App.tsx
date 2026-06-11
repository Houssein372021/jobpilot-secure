import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import ApplicationsPage from "./pages/applications/ApplicationsPage";
import DashboardPage from "./pages/dashboard/DashboardPage";
import ApplicationFormPage from "./pages/applications/ApplicationFormPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />

        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route element={<MainLayout />}>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/applications" element={<ApplicationsPage />} />
          <Route path="/applications/new" element={<ApplicationFormPage />} />
          <Route path="/applications/:id/edit" element={<ApplicationFormPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;