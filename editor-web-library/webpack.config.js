"use strict";
import CopyPlugin from "copy-webpack-plugin";
import path from "path";
import { fileURLToPath } from "url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
let loader;
loader = {
  test: /\.(t|j)sx?$/,
  use: {
    loader: 'swc-loader',
    options: {
      jsc: {
        "target": "es2019"
      }
    }
  },
  exclude: /node_modules/
};

export default {
  devtool: 'source-map',
  entry: {
    simple: './src/playground.ts'
  },
  mode: "production",
  stats: { warnings: false },
  ignoreWarnings: [
    {
      module: /module2\.js\?[34]/, // A RegExp
    },
    {
      module: /[13]/,
      message: /homepage/,
    },
    /warning from compiler/,
    (warning) => true,
  ],
  module: {
    rules: [
      loader, {
        test: /\.css$/i,
        use: ["style-loader", "css-loader"]
      }
    ]
  },
  resolveLoader: {
    alias: {
      "ace-code/src/requirejs/text": __dirname + "/node_modules/text-loader"
    },
    modules: [
      "node_modules", __dirname + "/node_modules"
    ]
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js']
  },
  output: {
    //https://stackoverflow.com/questions/61011841/source-map-error-error-invalid-url-how-to-fix-it
    devtoolModuleFilenameTemplate:
      'http://[namespace]/[resource-path]?[loaders]'
    ,
    filename: 'bundle.[name].js',
    path: __dirname + '/build'
  },
  optimization: {
    minimize: false
  },
  devServer: {
    //https://stackoverflow.com/questions/31602697/webpack-dev-server-cors-issue
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
      "Access-Control-Allow-Headers": "X-Requested-With, content-type, Authorization"
    },
    compress: false,
    port: 9001,
    client: {
      overlay: false
    }
  },
  plugins: [
    new CopyPlugin({
      patterns: [
        {
          from: "index.html",
          to: "."
        }, {
          from: "node_modules/ace-code/ace.d.ts",
          to: "ace.d.ts"
        }, {
          from: "node_modules/ace-code/ace-modes.d.ts",
          to: "ace-modes.d.ts"
        }, {
          from: "node_modules/ace-linters/types/language-provider.d.ts",
          to: "language-provider.d.ts"
        }, {
          from: "node_modules/ace-linters/types/types/language-service.d.ts",
          to: "language-service.d.ts"
        }
      ]
    })
  ]
};
