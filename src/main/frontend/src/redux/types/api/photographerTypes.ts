import { ReviewReportCause } from "types/ReviewReportCause";
import { DateTime } from "luxon";
import { BasicUserInfoResponse } from "./accountTypes";
import { Specialization } from "types/Specializations";

export interface BasicPhotographerInfo extends BasicUserInfoResponse {
   login: string;
   score: number;
   reviewCount: number;
   description: string;
   latitude: number;
   longitude: number;
   specializationList: Specialization[];
}

export interface PhotographerTableEntry {
   login: string;
   email: string;
   name: string;
   surname: string;
   reviewCount: number;
   score: number;
   specializations: string[];
   longitude: number;
   latitude: number;
}

export interface GetPhotographersListRequest {
   pageNo: number;
   recordsPerPage: number;
}

export interface ReportPhotographerReviewRequest {
   reviewId: number;
   cause: string;
}

export interface GetPhotographerReviewsResponse {
   pageNo: number;
   recordsPerPage: number;
   list: {
      authorLogin: string;
      id: number;
      name: string;
      surname: string;
      score: number;
      content: string;
      likeCount: string;
   }[];
}

export interface photographerReport {
   id: number;
   accountLogin: string;
   photographerLogin: string;
   cause: string;
   reviewed: boolean;
   createdAt: Date;
}

export interface reviewReport {
   id: number;
   accountLogin: string;
   reviewId: number;
   cause: string;
   reviewed: boolean;
   createdAt: Date;
   liked: boolean;
}

export interface GetPhotographerReviewsRequest {
   pageNo: number;
   recordsPerPage?: number;
   photographerLogin: string;
}

export interface addReviewRequest {
   photographerLogin: string;
   score: number;
   content: string;
}

export interface AvailabilityResponse {
   id: number;
   day: number;
   from: string;
   to: string;
}

export interface AvailabilityRequest {
   day: string;
   from: string;
   to: string;
}

export interface ReviewInfo {
   id: number;
   photographerLogin: string;
   reviewerLogin: string;
   score: number;
   likeCount: number;
   content: string;
   active: boolean;
   createdAt: Date;
}

export interface AddReservationRequest {
   photographerLogin: string;
   from: DateTime;
   to: DateTime;
}

export interface ReservationRequest {
   name?: string;
   order?: "asc" | "desc";
   all?: boolean;
   date: string;
}

export interface UserData {
   login: string;
   name: string;
   surname: string;
   email: string;
}

export interface ReservationResponse {
   id: number;
   photographer: UserData;
   client: UserData;
   from: string;
   to: string;
}

export interface ReportPhotographerRequest {
   photographerLogin: string;
   cause: string;
}

export interface SimpleReservation {
   id: number;
   from: string;
   to: string;
}

export interface DetailedPhotographerInfo extends BasicUserInfoResponse {
   login: string;
   score: number;
   reviewCount: number;
   description: string;
   latitude: number;
   longitude: number;
   specializations: Specialization[];
}

export interface PhotographerListResponse {
   allPages: number;
   allRecords: number;
   pageNo: number;
   recordsPerPage: number;
   list: DetailedPhotographerInfo[];
}

export interface PhotographerListRequest {
   query: string;
   pageNo: number;
   recordsPerPage: number;
}
