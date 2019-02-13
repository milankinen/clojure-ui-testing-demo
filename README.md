# Clojure UI testing demo

This repository contains codes for Reaktor #lisp meetup topic
"Concise UI testing with Clojure".

Slides: http://slides.com/mlareaktor/cuic


### Quickstart

Pre-requirements:

* `Java 8+`
* `docker`
* `docker-compose`

```bash
# start database
docker-compose up -d

# run tests
./lein t 
# start repl and app in port 5001
./lein repl
```

### Available git tags

* `base` - base application structure without API implementation
* `api` - API implemented
* `ui-tests` - UI tests written

## License

MIT
