"use strict";

let anki = require("./ankigame2048-export.json");
let fs = require('fs');

function retrieveUsers(path, properties) {
	let file_name = "data/"
	.concat(path.replace(/\//g, "_"))
	.concat(".csv");

	let header = properties.reduce((str, prop) => {
		return str.concat(prop).concat(",");
	}, "id".concat(","))
	.slice(0, -1)
	.concat("\n");

	fs.writeFile(file_name, header, err => {
		if(err) throw err;
	});
	let out = fs.createWriteStream(file_name, {flags: 'a'});

	let objectsJson = path
	.split("/")
	.reduce((obj, branch) => {
		return obj[branch];
	}, anki);

	Object.keys(objectsJson).map(key => {
		let currentUser = objectsJson[key];
		let csv = properties.reduce((str, prop) => {
			return str.concat(getProperty(currentUser, prop)).concat(",");
		}, key.concat(","))
		.slice(0, -1)
		.concat("\n");

		out.write(csv, 'utf8');
	});
}

function retrieveLogs(path, properties) {
	let file_name = "data/"
	.concat(path.replace(/\//g, "_"))
	.concat(".csv");

	let header = properties.reduce((str, prop) => {
		if(prop === "deckInfo") {
			return str + "deckName" + "," +
			"newTodayDay" + "," +
			"newTodayCount" + "," +
			"revTodayDay" + "," +
			"revTodayCount" + "," +
			"lrnTodayDay" + "," +
			"lrnTodayCount" + "," +
			"timeTodayDay" + "," +
			"timeTodayCount" + "," +
			"extendNew" + "," +
			"extendRev" + ","
		}
		return str.concat(prop).concat(",");
	}, "id".concat(","))
	.slice(0, -1)
	.concat("\n");

	fs.writeFile(file_name, header, err => {
		if(err) throw err;
	});
	let out = fs.createWriteStream(file_name, {flags: 'a'});

	let objectsJson = path
	.split("/")
	.reduce((obj, branch) => {
		return obj[branch];
	}, anki);

	Object.keys(objectsJson).map(key => {
		let currentLogType = objectsJson[key];
		Object.keys(currentLogType).map(key2 => {
			let currentLog = currentLogType[key2];
			let csv = properties.reduce((str, prop) => {
				return str.concat(getProperty(currentLog, prop)).concat(",");
			}, key2.concat(","))
			.slice(0, -1)
			.concat("\n");

			if(csv.split(",").length != 27) {
				console.log(csv);
				console.log("invalid line");
			}

			out.write(csv, 'utf8');
		});
	});
}

function getProperty(jsonObject, property) {
	let noDeck = ",,,,,,,,,,";
	if(jsonObject.hasOwnProperty(property)) {
		if(property === "logs") {
			return Object.keys(jsonObject[property]).length;
		}

		if(property === "deckInfo") {
			let val = jsonObject[property].replace(/\[/g, "").replace(/\]/g, "");
			let elems = val.split(",");
			if(elems.length == 11)
				return val;
			else if(elems.length < 11) {
				return val + ",,";
			}
			else if(elems.length > 11) {
				return elems.reverse().reduce((s, e, i) => {
					if(i === 0)
						return e;
					else if(i > 0 && i < 11)
						return e + "," + s;
					else
						return e + "|" + s;

				}, "");
			}
		}

		let val = jsonObject[property] + "";
		return val.replace(/\,/g, "|"); // remove commas
	}
	if(property === "deckInfo")
		return noDeck;
	return "";
}

let userProperties = ["logs", "nickName", "points", "bestScore", "date", "time"];
retrieveUsers("connection/users", userProperties);
retrieveUsers("independent/users", userProperties);
retrieveUsers("public/connection/users", userProperties);
retrieveUsers("public/independent/users", userProperties);

// let logProperties = ["cardAnswer", "cardEase", "cardInfo", "coinsInCard",
// "deckInfo", "dueDeckInfo", "earnedCoins", "earnedPoints", "elapsedTime",
// "favCard", "isFavCard", "logType", "pointsInCard", "totalCoins", "totalPoints",
// "userId", "date", "time"];
let logProperties = ["cardEase", "cardInfo", "coinsInCard", "deckInfo",
"earnedCoins", "earnedPoints", "elapsedTime",
"favCard", "isFavCard", "logType", "pointsInCard", "totalCoins", "totalPoints",
"userId", "date", "time"];
retrieveLogs("connection/logs", logProperties);
retrieveLogs("independent/logs", logProperties);
retrieveLogs("public/connection/logs", logProperties);
retrieveLogs("public/independent/logs", logProperties);