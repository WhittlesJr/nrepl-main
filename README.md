# nrepl-main

A Clojure library to provide a `-main` function for starting an [nREPL][nrepl] server.

## Usage

Add the library to your `:dependencies` in `project.clj`.

```clj
:dependencies [[nrepl-main "0.1.0-SNAPSHOT"]]
```

If you wish to have the main as the default main in your jar or uberjar file, then
add a `:main` for it in your `projec.clj` file.

```clj
:main nrepl.main
```

To override the defaults for the repl....


## License

Copyright © 2013 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[nrepl]: https://github.com/clojure/tools.nrepl "nREPL"
