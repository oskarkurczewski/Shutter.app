import React, { Suspense } from "react";
import "./style.scss";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import { ProtectedRoute } from "components/routes";
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

function App() {
   return (
      <Suspense fallback="loading...">
         <BrowserRouter>
            <Routes>
               <Route path="*" element={<NotFoundPage />} />

               <Route element={<PageLayout />}>
                  <Route path="" element={<HomePage />} />

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
                        path=":login/edit"
                        element={
                           <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                              <UserPages.EditUserAccountPage />
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

                  <Route path="dashboard" element={<DashboardPage />} />

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
         </BrowserRouter>
      </Suspense>
   );
}

export default App;
