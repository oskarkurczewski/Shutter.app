import React from "react";
import { Routes, Route } from "react-router-dom";
import { PageLayout } from "layout";
import { AccessLevel } from "types/AccessLevel";

import { DashboardPage } from "pages/dashboard";
import { HomePage } from "pages/home";
import { NotFoundPage } from "pages/not-found";
import { RegisterPage, LoginPage } from "pages/auth";
import { SettingsPage } from "pages/settings";
import * as UserPages from "pages/users";
import * as TokenBased from "pages/token-based";
import { PhotographerGalleryPage } from "pages/photographers/gallery";
import * as PhotographerPages from "pages/photographers";
import { ReservationsListPage } from "pages/users";
import { ProtectedRoute } from "components/routes";

export const AppRoutes = () => {
   return (
      <Routes>
         <Route element={<PageLayout />}>
            <Route path="*" element={<NotFoundPage />} />
            <Route path="" element={<HomePage />} />
            <Route path="dashboard" element={<DashboardPage />} />

            <Route
               path="login"
               element={
                  <ProtectedRoute roles={[AccessLevel.GUEST]}>
                     <LoginPage />
                  </ProtectedRoute>
               }
            />

            <Route
               path="register"
               element={
                  <ProtectedRoute roles={[AccessLevel.GUEST]}>
                     <RegisterPage />
                  </ProtectedRoute>
               }
            />

            <Route
               path="settings"
               element={
                  <ProtectedRoute
                     roles={[
                        AccessLevel.ADMINISTRATOR,
                        AccessLevel.MODERATOR,
                        AccessLevel.PHOTOGRAPHER,
                        AccessLevel.CLIENT,
                     ]}
                  >
                     <SettingsPage />
                  </ProtectedRoute>
               }
            ></Route>

            <Route
               path="photographers"
               element={<PhotographerPages.PhotographersListPage />}
            />

            <Route
               path="reservations"
               element={
                  <ProtectedRoute roles={[AccessLevel.CLIENT]}>
                     <ReservationsListPage />
                  </ProtectedRoute>
               }
            />

            <Route path="users">
               <Route
                  path=""
                  element={
                     <ProtectedRoute
                        roles={[AccessLevel.ADMINISTRATOR, AccessLevel.MODERATOR]}
                     >
                        <UserPages.UserAccountListPage />
                     </ProtectedRoute>
                  }
               />

               <Route
                  path="create"
                  element={
                     <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                        <UserPages.CreateUserAccountPage />
                     </ProtectedRoute>
                  }
               />

               <Route
                  path=":login/info"
                  element={
                     <ProtectedRoute
                        roles={[AccessLevel.ADMINISTRATOR, AccessLevel.MODERATOR]}
                     >
                        <UserPages.UserAccountInfoPage />
                     </ProtectedRoute>
                  }
               />
            </Route>

            {/* Photographer routes */}
            <Route path="profile">
               <Route path="" element={<PhotographerPages.PhotographerProfilePage />} />
               <Route
                  path=":login"
                  element={<PhotographerPages.PhotographerProfilePage />}
               />
               <Route
                  path="change-availability"
                  element={
                     <ProtectedRoute roles={[AccessLevel.PHOTOGRAPHER]}>
                        <PhotographerPages.ChangeAvailabilityPage />
                     </ProtectedRoute>
                  }
               />

               <Route
                  path="gallery"
                  element={
                     <ProtectedRoute roles={[AccessLevel.PHOTOGRAPHER]}>
                        <PhotographerGalleryPage />
                     </ProtectedRoute>
                  }
               />

               <Route
                  path="jobs"
                  element={
                     <ProtectedRoute roles={[AccessLevel.PHOTOGRAPHER]}>
                        <PhotographerPages.JobsListPage />
                     </ProtectedRoute>
                  }
               />
            </Route>

            <Route
               path="photographers"
               element={<PhotographerPages.PhotographersListPage />}
            />

            {/* Token-based routes */}
            <Route
               path="change-own-email/:token"
               element={<TokenBased.ChangeOwnEmailPage />}
            />

            <Route
               path="unblock-account/:token"
               element={
                  <ProtectedRoute roles={[AccessLevel.GUEST]}>
                     <TokenBased.UnblockOwnAccountPage />
                  </ProtectedRoute>
               }
            />

            <Route
               path="/confirm-registration/:token"
               element={
                  <ProtectedRoute roles={[AccessLevel.GUEST]}>
                     <TokenBased.ConfirmRegistrationPage />
                  </ProtectedRoute>
               }
            />

            <Route
               path="request-reset-password"
               element={
                  <ProtectedRoute roles={[AccessLevel.GUEST]}>
                     <TokenBased.RequestResetPasswordPage />
                  </ProtectedRoute>
               }
            />

            <Route
               path="password-reset/:token"
               element={
                  <ProtectedRoute roles={[AccessLevel.GUEST]}>
                     <TokenBased.ResetPasswordPage />
                  </ProtectedRoute>
               }
            />
         </Route>
      </Routes>
   );
};
