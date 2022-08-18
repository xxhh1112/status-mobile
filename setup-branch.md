## How to get this branch working:

Build the app:

make run-clojure
make run-metro
detox build --configuration ios.sim.debug

to run tests: 
detox build --configuration ios.sim.debug


To add more automatic tests:
Update create-test.js (line 16) and add in the other file names.
https://github.com/status-im/status-mobile/blob/jcaprani/visual-test/create-test.js#L15


Alternatively add other features in `./src/quo2/screens/${filename}-options.edn`

Then run `make generate-visual-test`

visual Tests configs are stored in `e2e/visual-tests`
and then the rests are run in the file `e2e/app-visual-tests.e2e.ts`

