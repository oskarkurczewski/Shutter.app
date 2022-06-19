import React from "react";
import { Routes, Route, useLocation } from "react-router-dom";

import { PageLayout } from "layout";
import { AccessLevel } from "types/AccessLevel";

import { DashboardPage } from "pages/dashboard";
import { HomePage } from "pages/home";
import { NotFoundPage } from "pages/not-found";
import { RegisterPage, LoginPage } from "pages/auth";
import { SettingsPage } from "pages/settings";
import { ResetPasswordPage } from "pages/token-based/reset-password";
import { RequestResetPasswordPage } from "pages/request-reset-password";
import * as UserPages from "pages/users";
import * as TokenBased from "pages/token-based";
import { PhotographerGalleryPage } from "pages/photographers/gallery";
import {
   PhotographersListPage,
   PhotographerProfilePage,
   ChangeAvailabilityPage,
   JobsListPage,
} from "pages/photographers";
import { ReservationsListPage } from "pages/users";
import { ProtectedRoute } from "components/routes";
import { AnimatePresence } from "framer-motion";

export const AppRoutes = () => {
   const location = useLocation();

   return (
      <AnimatePresence>
         <Routes location={location} key={location.pathname}>
            <Route path="*" element={<NotFoundPage />} />

            <Route element={<PageLayout />}>
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
                  path="request-reset-password"
                  element={
                     <ProtectedRoute roles={[AccessLevel.GUEST]}>
                        <RequestResetPasswordPage />
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

               <Route path="photographers" element={<PhotographersListPage />} />

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
                  <Route path=":login" element={<PhotographerProfilePage />} />
                  <Route
                     path="change-availability"
                     element={
                        <ProtectedRoute roles={[AccessLevel.PHOTOGRAPHER]}>
                           <ChangeAvailabilityPage />
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
                           <JobsListPage />
                        </ProtectedRoute>
                     }
                  />
               </Route>

               <Route path="photographers" element={<PhotographersListPage />} />

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
                  path="password-reset/:token"
                  element={
                     <ProtectedRoute roles={[AccessLevel.GUEST]}>
                        <ResetPasswordPage />
                     </ProtectedRoute>
                  }
               />
            </Route>
         </Routes>
      </AnimatePresence>
   );
};
