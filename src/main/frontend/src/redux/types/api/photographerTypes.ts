import { basicUserInfoResponse } from "./accountTypes";
import { ReviewReportCause } from "types/ReviewReportCause";

export interface photographerTableEntry {
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

export interface getPhotographersListRequest {
   pageNo: number;
   recordsPerPage: number;
}

export interface reportPhotographerReviewRequest {
   reviewId: number;
   cause: string;
}

export interface basicPhotographerInfo extends basicUserInfoResponse {
   score: number;
   reviewCount: number;
   description: string;
   latitude: number;
   longitude: number;
   specializationList: string[];
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
}

export interface getPhotographerReviewsResponse {
   pageNo: number;
   recordsPerPage: number;
   list: {
      id: number;
      name: string;
      surname: string;
      score: number;
      content: string;
      likeCount: string;
   }[];
}

export interface getPhotographerReviewsRequest {
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
