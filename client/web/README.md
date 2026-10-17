# das web client

React 19 + TypeScript + Vite, styled with Tailwind CSS v4, linted and formatted with Biome. Package manager is pnpm.

## Commands

| Command           | What it does                                                 |
| ----------------- | ------------------------------------------------------------ |
| `pnpm dev`        | Vite dev server with HMR                                     |
| `pnpm build`      | Typechecks with `tsc -b`, then bundles into `dist/`          |
| `pnpm lint`       | Biome checks formatting, import order and lint rules         |
| `pnpm fix`        | Same as `pnpm lint`, applying safe fixes and formatting      |
| `pnpm preview`    | Serves the built `dist/` locally                             |

Node 24 and the pnpm version pinned in `packageManager` are expected. Run `pnpm install` after cloning.
