import React, { Suspense } from "react";
import "./style.scss";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import { ProtectedRoute } from "components/routes";
import { PageLayout } from "layout";
import { AccessLevel } from "types/AccessLevel";
import { SuspenseLoader } from "components/suspense-loader";
import { getLoginPayload, getTokenExp } from "util/loginUtil";
import { useAppDispatch } from "redux/hooks";
import { login, logout } from "redux/slices/authSlice";

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
} from "pages/photographers";
import { ReportsPage } from "pages/reports";

function App() {
   const dispatch = useAppDispatch();

   if (localStorage.getItem("token") && Date.now() < getTokenExp()) {
      dispatch(login(getLoginPayload()));
   } else {
      dispatch(logout());
   }

   return (
      <Suspense fallback={<SuspenseLoader />}>
         <BrowserRouter>
            <Routes>
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
                  </Route>

                  <Route
                     path="/reports"
                     element={
                        <ProtectedRoute
                           roles={[AccessLevel.ADMINISTRATOR, AccessLevel.MODERATOR]}
                        >
                           <ReportsPage />
                        </ProtectedRoute>
                     }
                  />

                  <Route
                     path=":login/profile/gallery"
                     element={
                        <ProtectedRoute roles={[AccessLevel.PHOTOGRAPHER]}>
                           <PhotographerGalleryPage />
                        </ProtectedRoute>
                     }
                  />

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

                  <Route path="photographers" element={<PhotographersListPage />} />
               </Route>
            </Routes>
         </BrowserRouter>
      </Suspense>
   );
}

export default App;
