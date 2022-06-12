export const nameSurnameFirstLetterPattern = new RegExp(
   "^[A-ZĄĆĘŃŚŁÓŻŹ][a-ząćęńśłóżź]*$"
);
export const nameSurnamePattern = new RegExp("^[A-Za-zĄĆĘŃŚŁÓŻŹąćęńśłóżź]*$");
export const emailPattern = new RegExp(
   "^(?=.{1,64}@)[A-Za-z0-9_-]+(.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(.[A-Za-z0-9-]+)*(.[A-Za-z]{2,})$"
);
// TODO: fix password regex
export const passwordPattern = new RegExp(
   "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()\\-{}:;',?/*~$^+=<>-]).{8,64}$"
);
