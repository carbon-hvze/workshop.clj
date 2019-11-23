.EXPORT_ALL_VARIABLES:
.PHONY: test build

repl:
	clj -m "tools.repl" -p 3002

test:
	clj -m "tools.test"

clj:
	clj -m "tools.build" "target/app.jar"
