# Debugging

## Inspecting re-frame with re-frisk

`re-frisk` is a state visualization tool written by our very own Andrey
(@flexsurfer). With its help you can inspect the current state of app-db, watch
events, etc.

![re-frisk](images/debugging/re-frisk.png)

To start `re-frisk`, execute the following command:
```bash
$ yarn shadow-cljs run re-frisk-remote.core/start
```

or you can also use make:

```bash
$ make run-re-frisk
```

A server will be started at http://localhost:4567. It might show "not connected"
at first. Don't worry and just start using the app. The events and state will
populate.

More details about re-frisk are on the [project
page](https://github.com/flexsurfer/re-frisk).

## Enabling debug logs

Calls to `log/debug` will not be printed to the console by default. It can be
enabled under "Advanced settings" in the app:

![Enable Debug Logs](images/debugging/log-settings.png)

## Checking status-go logs

While status mobile runs, it saves logs from `status-go` to `geth.log` file.

### Checking logs from physical device

To obtain `geth.log` from physical device you need to shake it and in an opened
menu select "Share logs".

![Share logs](images/debugging/share-logs.jpeg)

### Checking logs from iOS Simulator

When developing with iOS simulator it is more convenient to see the `geth.log`
updates in real-time. To do this:

- Open Activity Monitor.
- Find the "StatusIm" app and doubleclick it.
- In the opened window select "Open files and ports" and find the full path to
  `geth.log` (note that it won't appear until you login to Status app).

![geth.log path](images/debugging/geth-path.png)

## Inspecting database content

Encrypted database can be found using commands:

```
cd ~/Library/Developer/CoreSimulator/Devices
find ./ -name accounts.sql
```

To get unencrypted database you need to export it first:

- Open the status app in simulator
- On login screen enter the correct password without logging in
- Using repl execute statement to export db:

```clojure
(re-frame.core/dispatch [:multiaccounts.login.ui/export-db-submitted])
```

- save generated `export.db` file

Now you can locate the `export.db` and open it with preferred db viewer.

**Android:**

```sh
adb root
adb pull /storage/emulated/0/Android/data/im.status.ethereum.debug/files/Download/export.db /path/to/store/export.db
```

If you're using a release build, change the path to
`/storage/emulated/0/Android/data/im.status.ethereum/files/Download`

**iOS**

```
cd ~/Library/Developer/CoreSimulator/Devices
find ./ -name export.db
```

## Tips
### Android - Use adb to tail and filter logs

```
adb shell tail -n 10 -f /storage/emulated/0/Android/data/im.status.ethereum.debug/files/Download/geth.log | grep 'waku.relay'
```

The adb [logcat](https://developer.android.com/tools/logcat) command is a
flexible alternative. Call `make android-logcat` to see relevant logs, or
customize it to your need, for example, the following command will only show
logs from `StatusModule:W` at the *warn* level or above.

```sh
adb logcat 'StatusModule:W' '*:S'
```
