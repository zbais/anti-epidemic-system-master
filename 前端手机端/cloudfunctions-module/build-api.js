const fs = require('fs')
const path = require('path')

const {
	builtinModules
} = require('module')
const rollup = require('rollup')
const commonjs = require('rollup-plugin-commonjs')
const resolve = require('rollup-plugin-node-resolve')

const args = process.argv
let apiList = args.slice(2)

const apiPath = path.resolve(__dirname, 'src/api')
const distPath = path.resolve(__dirname, '../cloudfunctions-aliyun/')

if (apiList.length === 0) {
	apiList = fs.readdirSync(apiPath)
}

console.log(`待处理API：${apiList}`)

async function build(apiName) {
	const apiNamePath = path.resolve(apiPath, apiName)
	const inputFile = path.resolve(apiNamePath, `${apiName}.js`)
	const outputFile = path.resolve(distPath, `${apiName}/index.js`)
	const inputOptions = {
		input: inputFile,
		external: builtinModules,
		plugins: [
			resolve(),
			commonjs()
		]
	}
	const outputOptions = {
		file: outputFile,
		exports: "named",
		format: 'cjs'
	}
	const bundle = await rollup.rollup(inputOptions)
	await bundle.write(outputOptions)
	console.log(`API[${apiName}] 输出成功`)
}

apiList.forEach((item) => {
	build(item)
})
