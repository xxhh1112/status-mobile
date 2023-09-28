# How to make changes to status-go


## Overview

To make changes to status-go, the best approach is to first identify the issue/feature and the changes to be made directly in status-go.
For any bugs we want to most likely write a failing test in status-go, and make sure that it passes, before we test on mobile.

Once you are quite confident that the status-go code is ready to be tested on mobile, you can test it locally by running the following command:

```
env STATUS_GO_SRC_OVERRIDE={your-status-go-directory} make run-{android/ios}
```

This will recompile status-go and run it on your device.

Once you have tested it locally and you think it's ready for a PR, create a PR in status-go, and you can point your local branch to that PR by running the script:

```
scripts/update-status-go.sh {your-branch-name}
```

This will update `status-go-version.json`, which you can commit and create a PR on mobile.


Once both PRs have been approved and are good to merge, you can follow the [merge guidelines](./merging-pr-process.md)
