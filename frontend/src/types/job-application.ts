export type ApplicationStatus =
  | "SAVED"
  | "APPLIED"
  | "INTERVIEW"
  | "OFFER"
  | "REJECTED"
  | "WITHDRAWN";

export type JobApplication = {
  id: string;
  companyName: string;
  jobTitle: string;
  location: string | null;
  contractType: string | null;
  status: ApplicationStatus;
  source: string | null;
  applicationUrl: string | null;
  notes: string | null;
  appliedAt: string | null;
  createdAt: string;
  updatedAt: string | null;
  followUpAt: string | null;
  favorite: boolean;
};