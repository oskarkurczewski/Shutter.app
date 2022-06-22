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
         invalidatesTags: ["Photo"],
      }),
      getPhotosRequest: builder.query<getPhotosResponse, getPhotosRequest>({
         query: (data) => ({ url: "profile/photo/list", method: "GET", params: data }),
         providesTags: (result, error, page) =>
            result
               ? [
                    ...result.list.map(({ id }) => ({ type: "Photo" as const, id })),
                    { type: "Photo", id: "PARTIAL-LIST" },
                 ]
               : [{ type: "Photo", id: "PARTIAL-LIST" }],
      }),
      likePhotoRequest: builder.mutation<void, number>({
         query: (id) => ({ url: `profile/photo/${id}/like`, method: "POST" }),
         invalidatesTags: (result, error, id) => [
            { type: "Photo", id },
            { type: "Photo", id: "PARTIAL-LIST" },
         ],
      }),
      deletePhotoRequest: builder.mutation<void, number>({
         query: (id) => ({ url: `profile/photo/${id}`, method: "DELETE" }),
         invalidatesTags: (result, error, id) => [
            { type: "Photo", id },
            { type: "Photo", id: "PARTIAL-LIST" },
         ],
      }),
      unlikePhoto: builder.mutation<void, number>({
         query: (id) => ({
            method: "POST",
            url: `profile/photo/${id}/unlike`,
         }),
         invalidatesTags: (result, error, id) => [
            { type: "Photo", id },
            { type: "Photo", id: "PARTIAL-LIST" },
         ],
      }),
   }),
});

export const {
   usePostPhotoRequestMutation,
   useGetPhotosRequestQuery,
   useLikePhotoRequestMutation,
   useDeletePhotoRequestMutation,
   useUnlikePhotoMutation,
} = PhotoService;
