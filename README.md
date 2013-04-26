# Rave

A wrapper on the simple WebDAV client library [Sardine][].

[Sardine]: https://github.com/lookfirst/sardine

## Example

```clj
(require '[me.sorella.rave :as rave])

(rave/with-server {:baseurl  "http://dav.example.com/"
                   :username "foo"
                   :password "bar"}
                   
  (make-directory "baz")
  (put "baz/qux" (java.io/input-stream "qux.txt") "text/plain"))
```

## License

MIT. IOW, do whatver you want.
