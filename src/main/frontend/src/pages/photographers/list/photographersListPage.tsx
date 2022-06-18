import React, { FC, useEffect, useState } from "react";
import styles from "./photographersListPage.module.scss";
import { useTranslation } from "react-i18next";
import type { TFunction } from "react-i18next";
import { MdKeyboardArrowRight } from "react-icons/md";
import { Button, Card, IconText, TextInput } from "components/shared";
import { FaSearch } from "react-icons/fa";
import { DetailedPhotographerInfo, PhotographerListRequest } from "redux/types/api";
import { Specialization } from "types/Specializations";
import { useNavigate } from "react-router-dom";
import type { NavigateFunction } from "react-router-dom";
import { useGetPhotographerListMutation } from "redux/service/photographerService";
import useDebounce from "hooks/useDebounce";

export const PhotographersListPage = () => {
   const { t } = useTranslation();
   const navigate = useNavigate();

   const [photographerSearchFilters, setPhotographerSearchFilters] =
      useState<PhotographerListRequest>({
         query: "",
         pageNo: 1,
         recordsPerPage: 25,
      });
   const [expandFilters, setExpandFilters] = useState(true);

   const debouncedFilters = useDebounce<PhotographerListRequest>(
      photographerSearchFilters,
      200
   );

   const [
      getPhotographers,
      {
         data: photographersListResponse = {
            allPages: 0,
            allRecords: 0,
            pageNo: 1,
            recordsPerPage: 25,
            list: [],
         },
      },
   ] = useGetPhotographerListMutation();

   useEffect(() => {
      getPhotographers(photographerSearchFilters);
   }, [debouncedFilters]);

   const handleChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
      const name = e.target.name;
      setPhotographerSearchFilters({
         ...photographerSearchFilters,
         [name]: e.target.value,
      });
   };

   return (
      <>
         <section className={`${styles.container}`}>
            <div className={styles.header}>
               <span className="category-title">{t("global.label.filters")}</span>
               <div className={styles.delimiter} />
               <button onClick={() => setExpandFilters(!expandFilters)}>
                  <MdKeyboardArrowRight
                     className={expandFilters ? "" : styles.expanded}
                  />
               </button>
            </div>
            <div className={`${styles.content} ${expandFilters ? "" : styles.hidden}`}>
               <Card className={styles.card}>
                  <p className="section-title">
                     {t("photographer_list_page.search_photographer")}
                  </p>
                  <TextInput
                     name="query"
                     className={styles.input}
                     icon={<FaSearch />}
                     value={photographerSearchFilters.query}
                     onChange={handleChange}
                  />
               </Card>
            </div>
         </section>

         <section className={styles.container}>
            <div className={styles.header}>
               <span className="category-title">
                  {t("photographer_list_page.photographers")}
               </span>
               <div className={styles.delimiter} />
               <div className={styles.functional}>
                  <p>{t("photographer_list_page.results", { count: 69 })}</p>
                  <DisabledDropdown label="LIST" />
                  <DisabledDropdown label="A-Z" />
               </div>
            </div>
            <div className={`${styles.content} ${styles.list}`}>
               {photographersListResponse.list?.map((obj) => (
                  <ListElement data={obj} t={t} navigate={navigate} key={obj.login} />
               ))}
               {/* <ListElement role={role} data={data} t={t} navigate={navigate} />
               <ListElement role={role} data={data} t={t} navigate={navigate} />
               <ListElement role={role} data={data} t={t} navigate={navigate} /> */}
            </div>
         </section>
      </>
   );
};

interface DisabledDropdownProps {
   label: string;
}

const DisabledDropdown: FC<DisabledDropdownProps> = ({ label }) => {
   return (
      <div className={styles.dropdown}>
         <p className="label">{label}</p>
         <MdKeyboardArrowRight />
      </div>
   );
};

interface ListElementProps {
   data?: DetailedPhotographerInfo;
   t: TFunction<"translation", undefined>;
   navigate: NavigateFunction;
}

const ListElement: FC<ListElementProps> = ({ data, t, navigate }) => {
   const { name, surname, specializations, score, reviewCount, login } = data;

   return (
      <Card className={`${styles.card} ${styles["list_element"]}`}>
         <div className={styles["avatar_wrapper"]}>
            <div className={styles.avatar}>
               <img
                  src="https://cdn.galleries.smcloud.net/t/galleries/gf-hTB5-Uktt-KEJc_norbert-dis-gierczak-664x442.jpg"
                  alt="fotograf"
               />
            </div>
            <div>
               <p className="section-title">
                  {name} {surname}
               </p>
               <p className="label">{t("photographer_list_page.photographer")}</p>
            </div>
         </div>
         <div className={styles.specialization_container}>
            {specializations.map((v) => (
               <SpecializationTag
                  key={v}
                  text={t(`global.specialization.${v.toLocaleLowerCase()}`)}
                  specialization={v}
               />
            ))}
         </div>

         <div className={styles.score_container}>
            <p className="label">
               {score.toPrecision(2)} (
               {t("photographer_list_page.reviews", { count: reviewCount })})
            </p>
         </div>

         <div className={styles.button_container}>
            <Button
               className={styles.gallery_button}
               onClick={() => navigate(`/profile/${login}`)}
            >
               {t("photographer_list_page.profile")}
            </Button>
            <Button onClick={() => navigate(`/profile/${login}/#callendar`)}>
               {t("photographer_list_page.hire")}
            </Button>
         </div>
      </Card>
   );
};

interface SpecializationTagProps {
   text: string;
   specialization: Specialization;
}

const SpecializationTag: FC<SpecializationTagProps> = ({ text, specialization }) => {
   switch (specialization) {
      case Specialization.SPECIALIZATION_LANDSCAPE:
         return <IconText className={styles.specialization} color="green" text={text} />;
      case Specialization.SPECIALIZATION_PHOTOREPORT:
         return <IconText className={styles.specialization} color="red" text={text} />;
      case Specialization.SPECIALIZATION_PRODUCT:
         return <IconText className={styles.specialization} color="blue" text={text} />;
      case Specialization.SPECIALIZATION_STUDIO:
         return <IconText className={styles.specialization} color="purple" text={text} />;
      default:
         return <IconText className={styles.specialization} text="..." />;
   }
};
