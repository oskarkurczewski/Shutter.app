import React, { Suspense, useEffect } from "react";
import "./style.scss";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import DashboardPage from "pages/dashboard/dashboardPage";
import PageLayout from "layout/Layout";
import Homepage from "pages/home/homePage";
import NotFound404 from "pages/not-found/notFoundPage";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { getLoginPayload, getTokenExp } from "util/loginUtil";
import { login, logout } from "redux/slices/authSlice";
import ProtectedRoute from "components/routes/protected-route/ProtectedRoute";
import { AccessLevel } from "types/AccessLevel";
import RegisterPage from "pages/auth/register/registerPage";
import CreateAccountPage from "pages/users/create/createUserAccountPage";
import AccountListPage from "pages/users/list/userAccountListPage";
import EditAccountPage from "pages/users/edit/editUserAccountPage";
import SettingsPage from "pages/settings/settingsPage";
import ResetPasswordPage from "pages/reset-password/resetPasswordPage";
import UnblockOwnAccountPage from "pages/token-based/unblock-own-account/unblockOwnAccountPage";
import LoginPage from "pages/auth/login/loginPage";
import Button from "components/shared/button/Button";
import { push, remove } from "redux/slices/toastSlice";
import { useRefreshTokenMutation } from "redux/service/api";
import RequestResetPasswordPage from "pages/request-reset-password/requestResetPasswordPage";
import ConfirmRegistrationPage from "pages/token-based/confirm-registration/confirmRegistrationPage";
import ChangeOwnEmailPage from "pages/token-based/change-own-email/changeOwnEmailPage";
import AccountInfoPage from "pages/users/info/userAccountInfoPage";

function App() {
   const dispatch = useAppDispatch();
   const exp = useAppSelector((state) => state.auth.exp);
   const [refreshToken] = useRefreshTokenMutation();

   if (localStorage.getItem("token") && Date.now() < getTokenExp()) {
      dispatch(login(getLoginPayload()));
   } else {
      dispatch(logout());
   }

   const data = {
      name: "refreshToken",
      label: "Powiadomienie",
      text: "Twoja sesja niedługo wygaśnie, kliknij w przycisk poniżej, aby ją przedłużyć!",
      content: (
         <Button
            onClick={async (e) => {
               e.preventDefault();
               try {
                  const token = await refreshToken({}).unwrap();
                  localStorage.setItem("token", token.token);
                  dispatch(login(getLoginPayload()));
                  dispatch(remove("refreshToken"));
               } catch (err) {
                  return;
               }
            }}
         >
            Przedłuż
         </Button>
      ),
   };

   useEffect(() => {
      if (exp !== 0 && exp - Date.now() < 1000 * 60 * 2) dispatch(push(data));

      const timeoutID = setInterval(() => {
         if (exp !== 0 && exp - Date.now() < 1000 * 60 * 2) {
            dispatch(push(data));
         }
      }, 1000 * 60 * 0.5);

      return () => {
         clearTimeout(timeoutID);
      };
   }, [exp]);

   return (
      <Suspense fallback="loading...">
         <BrowserRouter>
            <Routes>
               <Route path="*" element={<NotFound404 />} />

               <Route element={<PageLayout />}>
                  <Route path="" element={<Homepage />} />

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
                              <AccountListPage />
                           </ProtectedRoute>
                        }
                     />

                     <Route
                        path="create"
                        element={
                           <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                              <CreateAccountPage />
                           </ProtectedRoute>
                        }
                     />

                     <Route
                        path=":login/edit"
                        element={
                           <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                              <EditAccountPage />
                           </ProtectedRoute>
                        }
                     />

                     <Route
                        path=":login/info"
                        element={
                           <ProtectedRoute
                              roles={[AccessLevel.ADMINISTRATOR, AccessLevel.MODERATOR]}
                           >
                              <AccountInfoPage />
                           </ProtectedRoute>
                        }
                     />
                  </Route>

                  <Route path="dashboard" element={<DashboardPage />} />

                  {/* Token-based routes */}
                  <Route
                     path="change-own-email/:token"
                     element={<ChangeOwnEmailPage />}
                  />

                  <Route
                     path="unblock-account/:token"
                     element={
                        <ProtectedRoute roles={[AccessLevel.GUEST]}>
                           <UnblockOwnAccountPage />
                        </ProtectedRoute>
                     }
                  />

                  <Route
                     path="/confirm-registration/:token"
                     element={
                        <ProtectedRoute roles={[AccessLevel.GUEST]}>
                           <ConfirmRegistrationPage />
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

export const apiUrl = "https://studapp.it.p.lodz.pl:8002/api";

export default App;
