// #ifdef H5
const XLSX = require('xlsx/dist/xlsx.core.min.js')
export function excelToJson(arrayBuffer) {
	const data = new Uint8Array(arrayBuffer)
	const workbook = XLSX.read(data, {
		type: 'array'
	})
	const result = XLSX.utils.sheet_to_json(workbook.Sheets[workbook.SheetNames[0]])
	return result
}

export function jsonToExcel(jsonArray, sheetName) {
	const workbook = XLSX.utils.book_new()
	const worksheet = XLSX.utils.json_to_sheet(jsonArray)
	XLSX.utils.book_append_sheet(workbook, worksheet, sheetName)
	const wopts = {
		bookType: 'xlsx',
		bookSST: false,
		type: 'array'
	}
	const wbout = XLSX.write(workbook, wopts)
	return wbout
}

export default {
	excelToJson,
	jsonToExcel
}
// #endif
