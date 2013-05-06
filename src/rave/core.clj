(ns rave.core
  (:import [com.github.sardine SardineFactory]
           [java.io InputStream]))


(def ^:dynamic *sardine*)
(def ^:dynamic *baseurl* "")


(defmacro with-server
  "Initialises a new Sardine instance for the block."
  [options & body]
  `(let [username# (:username ~options)
         password# (:password ~options)
         proxy#    (:proxy ~options)
         gzip#     (:gzip ~options)
         baseurl#  (or (:baseurl ~options) *baseurl*)]
     (binding [*sardine* (SardineFactory/begin username# password# proxy#)
               *baseurl* baseurl#]
       (when gzip# (.enableCompression *sardine*))
       ~@body
       (.shutdown *sardine*))))
  

(defn with-baseurl
  "Prepends the baseurl to the given url."
  [url]
  (str *baseurl* url))


(defn as-directory
  "Returns a valid collection URI."
  [url]
  (if (.endsWith url "/") url
                          (str url "/")))


(defn children
  "Retrieves a list of `DavResource` objects inside a given URL.

The URL should properly end with a `/` if the resource points to a
Collection.

The `depth` argument is optional and defaults to 1. When set, it defines
the depth of the children that should be returned."
  [url & [depth]]
  (.list *sardine* (with-baseurl url) depth))


(defn retrieve
  "Returns a Stream for a resource's contents."
  [url]
  (.get *sardine* (with-baseurl url)))


(defn put
  "Sends a file to a DAV server."
  [url #^InputStream stream & [mime expect-continue?]]
  (.put *sardine* (with-baseurl url) stream mime expect-continue?))


(defn delete
  "Removes a resource from a DAV server."
  [url]
  (.delete *sardine* (with-baseurl url)))


(defn make-directory
  "Creates a Collection on the DAV server."
  [url]
  (.delete *sardine* (as-directory (with-baseurl url))))


(defn move
  "Moves a resource from one place to another on a DAV server."
  [from to]
  (.move *sardine* (with-baseurl from) (with-baseurl to)))


(defn copy
  "Copies a resource from one place to another on a DAV server."
  [from to]
  (.copy *sardine* (with-baseurl from) (with-baseurl to)))


(defn exists?
  "Verifies if a resource exists on a DAV server."
  [url]
  (.exists *sardine* (with-baseurl url)))


(defn add-properties
  "Adds custom properties to a DAV resource."
  [url props]
  (when props
    (.setCustomProps *sardine* (with-baseurl url) props)))


(defn remove-properties
  "Removes custom properties from a DAV resource."
  [url props]
  (when props
    (.setCustomProps *sardine* (with-baseurl url) {} props)))