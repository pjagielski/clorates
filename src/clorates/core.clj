(ns clorates.core
  (:require       
    [clorates.db :refer [transform-project transform-release transform-review 
      transform-release-with-project get-latest-releases get-project-by-name 
      get-project-releases get-project-reviews get-projects-with-all-tags-from]]      
    [clorates.common :refer [parse-int]]      
    [clorates.props :refer [props]]      
    [clojure.string :refer [split]]
    [ring.middleware.params :refer [wrap-params]]                  
    [ring.middleware.format :refer (wrap-restful-format)]
    [compojure.core :refer [defroutes routes ANY GET context]]
    [compojure.route :as route]
    [compojure.handler :as handler :refer (site)]
    [swag.core :refer (swagger-routes GET- defroutes- errors set-base)]
    [swag.model :refer (defmodel wrap-swag)]
    [noir.io :as io]
    [clabango.parser :as parser]
    [metrics.ring.expose :refer [expose-metrics-as-json]]
    [metrics.ring.instrument :refer [instrument]]))

(def base-url (or (get props "app.baseUrl") "http://localhost:3000"))

(set-base base-url)

(defmodel project 
  :name {:type "string"} :url {:type "string"}
  :tags {:type "array" :items {:type "string"}}
  :reviews_count {:type "int"} :overall_rank {:type "double"}
  :lang {:type "string"} :category {:type "string"})

(defmodel review
  :usability {:type "int"} :simplicity {:type "int"} 
  :docs {:type "int"} :community {:type "int"} 
  :username {:type "string"})

(defmodel release
  :version_no {:type "string"} :date {:type "date-time"})

(defroutes- releases {:path "/releases"}
  (GET- "/releases" 
    [^{:paramType "query" :dataType "int" :required false} limit
     ^{:paramType "query" :dataType "int" :required false} offset] 
    {:nickname "getReleases" :summary "List releases" :responseClass "Release"}
    {:status 200 :body (map transform-release-with-project
        (get-latest-releases (or limit 10) (or offset 0)))}))

(defroutes- projects {:path "/projects" :description "Read-only access to available open-source projects"}

  (GET- "/projects" 
    [^{:paramType "query" :dataType "string" :required true} tags
     ^{:paramType "query" :dataType "int" :required false} limit
     ^{:paramType "query" :dataType "int" :required false} offset] 
    {:nickname "getProjects" :summary "List projects by tags" :responseClass "Project"}
    (if (empty? tags) {:status 400 :body {:message "Tags not provided"}}
      {:status 200 :body (map transform-project 
        (get-projects-with-all-tags-from 
          (split tags #",") (or limit 10) (or offset 0)))}))

  (GET- "/projects/:name" [^:string name]
    {:nickname "getProject" :summary "Get project by name"
     :responseClass "Project"}
    {:status 200 :body (transform-project (get-project-by-name name))})

  (GET- "/projects/:name/reviews" 
    [^:string name
     ^{:paramType "query" :dataType "int" :required false} limit
     ^{:paramType "query" :dataType "int" :required false} offset]
    {:nickname "getProjectReviews" :summary "List all reviews of given project"
     :responseClass "Review"}
    {:status 200 :body (map transform-review 
      (get-project-reviews name (or limit 10) (or offset 0)))})

  (GET- "/projects/:name/releases" [^:string name]
    {:nickname "getProjectReleases" :summary "List all releases of given project"
     :responseClass "Release"}
    {:status 200 :body 
      (map transform-release (get-project-releases name))}))

(def app
  (-> (routes (swagger-routes "1.0.0") releases projects (route/not-found "Not Found"))
      (wrap-swag)
      (handler/api)
      (wrap-restful-format :formats [:json-kw :edn])))

(def app (expose-metrics-as-json (instrument app) "/stats/"))
