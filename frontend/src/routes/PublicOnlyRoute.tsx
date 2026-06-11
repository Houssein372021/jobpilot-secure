import { Navigate, Outlet } from "react-router-dom";
import { isAuthenticated } from "../utils/auth-storage";

function PublicOnlyRoute() {
  if (isAuthenticated()) {
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />;
}

export default PublicOnlyRoute;