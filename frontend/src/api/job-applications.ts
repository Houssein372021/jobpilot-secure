import { http } from "./http";
import type { ApplicationStatus, JobApplication } from "../types/job-application";
import type { PageResponse } from "../types/pagination";

export type JobApplicationsSortBy =
  | "createdAt"
  | "updatedAt"
  | "companyName"
  | "jobTitle"
  | "status"
  | "favorite";

export type SortDirection = "asc" | "desc";

export type FavoriteFilter = "all" | "true" | "false";

export type JobApplicationsQuery = {
  page?: number;
  size?: number;
  search?: string;
  status?: ApplicationStatus | "ALL";
  favorite?: FavoriteFilter;
  sortBy?: JobApplicationsSortBy;
  direction?: SortDirection;
};

export type JobApplicationRequest = {
  companyName: string;
  jobTitle: string;
  location: string | null;
  contractType: string | null;
  status: ApplicationStatus;
  source: string | null;
  applicationUrl: string | null;
  notes: string | null;
  followUpAt: string | null;
};

export async function getJobApplications(
  query: JobApplicationsQuery = {}
): Promise<PageResponse<JobApplication>> {
  const params = new URLSearchParams();

  params.set("page", String(query.page ?? 0));
  params.set("size", String(query.size ?? 10));
  params.set("sortBy", query.sortBy ?? "createdAt");
  params.set("direction", query.direction ?? "desc");

  if (query.search?.trim()) {
    params.set("search", query.search.trim());
  }

  if (query.status && query.status !== "ALL") {
    params.set("status", query.status);
  }

  if (query.favorite && query.favorite !== "all") {
    params.set("favorite", query.favorite);
  }

  const response = await http.get<PageResponse<JobApplication>>(
    `/job-applications?${params.toString()}`
  );

  return response.data;
}

export async function getJobApplicationById(id: string): Promise<JobApplication> {
  const response = await http.get<JobApplication>(`/job-applications/${id}`);
  return response.data;
}

export async function createJobApplication(
  request: JobApplicationRequest
): Promise<JobApplication> {
  const response = await http.post<JobApplication>("/job-applications", request);
  return response.data;
}

export async function updateJobApplication(
  id: string,
  request: JobApplicationRequest
): Promise<JobApplication> {
  const response = await http.put<JobApplication>(`/job-applications/${id}`, request);
  return response.data;
}