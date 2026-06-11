import { http } from "./http";
import type { DashboardActionSummary, DashboardStats } from "../types/dashboard";
import type { JobApplication } from "../types/job-application";

export async function getDashboardStats(): Promise<DashboardStats> {
  const response = await http.get<DashboardStats>("/dashboard/stats");
  return response.data;
}

export async function getDashboardActionSummary(): Promise<DashboardActionSummary> {
  const response = await http.get<DashboardActionSummary>("/dashboard/action-summary");
  return response.data;
}

export async function getTodayFollowUps(): Promise<JobApplication[]> {
  const response = await http.get<JobApplication[]>("/dashboard/today-follow-ups");
  return response.data;
}

export async function getOverdueFollowUps(): Promise<JobApplication[]> {
  const response = await http.get<JobApplication[]>("/dashboard/overdue-follow-ups");
  return response.data;
}

export async function getUpcomingFollowUps(): Promise<JobApplication[]> {
  const response = await http.get<JobApplication[]>("/dashboard/upcoming-follow-ups");
  return response.data;
}