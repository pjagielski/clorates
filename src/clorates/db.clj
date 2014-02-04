(ns clorates.db
    (:require 
        [clojure.string :as string]    
        [korma.db :refer [defdb h2 postgres]]
        [korma.core :refer :all]
        [clorates.props :refer [props]]
        [clorates.common :refer :all]))

(def devrates-dev (h2 
  {:db "resources/db/devrates"
   :user "sa"
   :password ""
   :naming {:keys string/lower-case :fields string/upper-case}}))

(def devrates-prod (postgres 
  {:db (get props "db.name")
   :user (get props "db.user")
   :password (get props "db.pass")
   :host (get props "db.host")
   :port (get props "db.port")}))

(defdb korma-db devrates-dev)

(defentity users
  (pk :id)
  (table :user))  

(defentity tags
  (pk :id)
  (table :tag))

(defentity project-statistics
  (pk :project_id)
  (table :v_projects_statistics))

(defentity projects
    (pk :id)
    (table :project)
    (many-to-many tags :project_tags)
    (has-one project-statistics))

(defentity releases
  (pk :id)
  (table :release)
  (belongs-to projects))

(defentity reviews
  (pk :id)
  (table :rank)
  (belongs-to projects)
  (belongs-to users))

(defentity project_tags)

(defn with-default-limit-and-offset 
  ([fun] (with-default-limit-and-offset fun 10 0))
  ([fun lim] (with-default-limit-and-offset fun lim 0))
  ([fun lim off] (partial fun lim off)))

(defn get-all-projects 
  ([lim off] 
    (select projects (with tags)
      (limit lim) (offset off))))

(defn get-projects-with-ids 
  ([ids lim off]
    (select projects (with project-statistics) (with tags) 
      (where {:id [in ids]})
      (limit lim) (offset off) 
      (order :v_projects_statistics.overall_rank :desc))))

(defn get-projects-with-any-tag-from [tagnames]    
  (select projects (join tags) 
    (where {:tag.name [in tagnames]})))

(defn get-projects-ids-with-all-tags-from [tagnames]
  (map key (having-vals-count-of 
    (group-by :id (get-projects-with-any-tag-from tagnames)) 
    (count tagnames))))

(defn get-projects-with-all-tags-from [tagnames lim off]
  (get-projects-with-ids 
    (get-projects-ids-with-all-tags-from tagnames) lim off))

(defn get-project-by-name [name]
  (first (select projects (with project-statistics) (with tags)
    (where {:name name}))))

(defn get-project-releases [name]
  (select releases (with projects) 
    (where {:project.name name})
    (order :date :desc)))

(defn get-project-reviews [name lim off]
  (select reviews (with users) (with projects) 
    (where {:project.name name})
    (limit lim) (offset off))) 

(defn get-latest-releases [lim off]
  (select releases (with projects)
    (limit lim) (offset off)
    (order :date :desc)))

;; transformers

(defn transform-tag-name [tag]
  (:name tag))

(defn transform-project [proj]
  (let [tags (:tag proj)
        keys-to-keep [:name :url :tags :reviews_count :overall_rank :lang :category]
        transformed-tags (map transform-tag-name tags)
        proj-with-lowerase-keys (keys-to-lowercase proj)] 
    (keep-only-keys keys-to-keep
      (assoc proj-with-lowerase-keys :tags transformed-tags))))

(defn transform-release [rel]
  (keep-only-keys [:version_no :date] 
    (keys-to-lowercase rel)))

(defn transform-review [rev]
  (keep-only-keys [:usability :simplicity :docs :community :username]
    (keys-to-lowercase rev)))

(defn transform-release-with-project [rel] 
  (keep-only-keys [:project_name :version_no :date]
    (assoc rel :project_name (:name rel))))