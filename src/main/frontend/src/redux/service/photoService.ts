import { api } from "./api";
import {
   getPhotosRequest,
   getPhotosResponse,
   postPhotoRequest,
} from "redux/types/api/photoTypes";

const PhotoService = api.injectEndpoints({
   endpoints: (builder) => ({
      postPhotoRequest: builder.mutation<void, postPhotoRequest>({
         query: (data) => ({ url: "profile/photo", method: "POST", body: data }),
      }),
      getPhotosRequest: builder.query<getPhotosResponse, getPhotosRequest>({
         query: (data) => ({ url: "profile/photo/list", method: "GET", params: data }),
      }),
      likePhotoRequest: builder.mutation<void, number>({
         query: (id) => ({ url: `profile/photo/${id}/like`, method: "POST" }),
      }),
      deletePhotoRequest: builder.mutation<void, number>({
         query: (id) => ({url: `profile/photo/${id}`, method: "DELETE"})
      })
   }),
});

export const {
   usePostPhotoRequestMutation,
   useGetPhotosRequestQuery,
   useLikePhotoRequestMutation,
   useDeletePhotoRequestMutation,
} = PhotoService;
