# Scala.js Minimum cross compile exmaple

Cross compile trial

## Run as scala

Run

```
sbt run
```

Test with specs2

```
sbt test
```

## Run as javascript

Development

```
sbt packageJS
open assets/index-dev.html
```

Production(compile time 2min~)

```
sbt optimizeJS
open assets/index.html
```
