import { basicUserInfoResponse } from "./accountTypes";

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

export interface basicPhotographerInfo extends basicUserInfoResponse {
   score: number;
   reviewCount: number;
   description: string;
   latitude: number;
   longitude: number;
   specializationList: string[];
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
