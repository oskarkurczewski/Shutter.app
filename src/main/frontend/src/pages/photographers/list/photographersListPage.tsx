import React, { FC, useState } from "react";
import styles from "./photographersListPage.module.scss";
import { useTranslation } from "react-i18next";
import type { TFunction } from "react-i18next";
import { MdKeyboardArrowRight } from "react-icons/md";
import { Button, Card, IconText, TextInput } from "components/shared";
import { FaSearch } from "react-icons/fa";
import { BasicPhotographerInfo } from "redux/types/api";
import { Specialization } from "types/Specializations";
import { useNavigate } from "react-router-dom";
import type { NavigateFunction } from "react-router-dom";

export const PhotographersListPage = () => {
   const { t } = useTranslation();
   const navigate = useNavigate();

   const [photographerSearchQuery, setPhotographerSearchQuery] = useState("");
   const [expandFilters, setExpandFilters] = useState(true);

   const role = t("photographer_list_page.photographer");

   const data = {
      version: 0,
      login: "majster2",
      email: "dis@ork.pl",
      name: "Norbert",
      surname: "Gierczak",
      score: 4.52137,
      reviewCount: 2137,
      description: "opis",
      latitude: 0,
      longitude: 0,
      specializationList: [
         Specialization.SPECIALIZATION_LANDSCAPE,
         Specialization.SPECIALIZATION_PHOTOREPORT,
         Specialization.SPECIALIZATION_PRODUCT,
         Specialization.SPECIALIZATION_STUDIO,
      ],
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
                     className={styles.input}
                     icon={<FaSearch />}
                     value={photographerSearchQuery}
                     onChange={(e) => setPhotographerSearchQuery(e.target.value)}
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
               <ListElement role={role} data={data} t={t} navigate={navigate} />
               <ListElement role={role} data={data} t={t} navigate={navigate} />
               <ListElement role={role} data={data} t={t} navigate={navigate} />
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
   data?: BasicPhotographerInfo;
   role: string;
   t: TFunction<"translation", undefined>;
   navigate: NavigateFunction;
}

const ListElement: FC<ListElementProps> = ({ data, role, t, navigate }) => {
   const { name, surname, specializationList, score, reviewCount, login } = data;

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
               <p className="label">{role}</p>
            </div>
         </div>
         <div className={styles.specialization_container}>
            {specializationList.map((v) => (
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
               onClick={() => navigate(`/profile/${login}/#gallery`)}
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
