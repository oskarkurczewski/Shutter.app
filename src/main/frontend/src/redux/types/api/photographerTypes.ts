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
      id: number;
      name: string;
      surname: string;
      score: number;
      content: string;
      likeCount: string;
   }[];
}

export interface GetPhotographerReviewsRequest {
   pageNo: number;
   recordsPerPage?: number;
   photographerLogin: string;
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
