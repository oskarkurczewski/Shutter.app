export interface postPhotoRequest {
   title: string;
   description: string;
   data: string;
}

export interface getPhotosResponse {
   recordsPerPage: number;
   pageNo: number;
   allRecords: number;
   allPages: number;
   list: {
      id: number;
      title: string;
      description?: string;
      s3Url: string;
      likeCount: number;
      createdAt: any;
      liked: boolean;
   }[];
}

export interface getPhotosRequest {
   photographerLogin: string;
   recordsPerPage?: number;
   pageNo?: number;
}
