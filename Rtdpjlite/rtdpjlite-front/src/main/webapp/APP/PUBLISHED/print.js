//** http://printjs.crabbly.com/ **/
//** https://github.com/crabbly/print.js **/
//** MIT Licencse **/
(function(root, factory) {
	if (typeof define === 'function' && define.amd) {
		// AMD. Register as an anonymous module.
		define([], factory);
	} else if (typeof exports === 'object') {
		// Node. Does not work with strict CommonJS, but
		// only CommonJS-like environments that support module.exports,
		// like Node.
		module.exports = factory();
	} else {
		// Browser globals (root is window)
		root.download = factory();
	}
}(this, function() {
	return function download(data, strFileName, strMimeType) {
		var self = window, // this script is only for browsers anyway...
			defaultMime = "application/octet-stream", // this default mime also triggers iframe downloads
			mimeType = strMimeType || defaultMime,
			payload = data,
			url = !strFileName && !strMimeType && payload,
			anchor = document.createElement("a"),
			toString = function(a) {
				return String(a);
			},
			myBlob = (self.Blob || self.MozBlob || self.WebKitBlob || toString),
			fileName = strFileName || "download",
			blob,
			reader;
		myBlob = myBlob.call ? myBlob.bind(self) : Blob ;

		if (String(this) === "true") { //reverse arguments, allowing download.bind(true, "text/xml", "export.xml") to act as a callback
			payload = [ payload, mimeType ];
			mimeType = payload[0];
			payload = payload[1];
		}
		if (url && url.length < 2048) { // if no filename and no mime, assume a url was passed as the only argument
			fileName = url.split("/").pop().split("?")[0];
			anchor.href = url; // assign href prop to temp anchor
			if (anchor.href.indexOf(url) !== -1) { // if the browser determines that it's a potentially valid url path:
				var ajax = new XMLHttpRequest();
				ajax.open("GET", url, true);
				ajax.responseType = 'blob';
				ajax.onload = function(e) {
					download(e.target.response, fileName, defaultMime);
				};
				setTimeout(function() {
					ajax.send();
				}, 0); // allows setting custom ajax headers using the return:
				return ajax;
			} // end if valid url?
		} // end if url?


		//go ahead and download dataURLs right away
		if (/^data\:[\w+\-]+\/[\w+\-]+[,;]/.test(payload)) {

			if (payload.length > (1024 * 1024 * 1.999) && myBlob !== toString) {
				payload = dataUrlToBlob(payload);
				mimeType = payload.type || defaultMime;
			} else {
				return navigator.msSaveBlob ? // IE10 can't do a[download], only Blobs:
					navigator.msSaveBlob(dataUrlToBlob(payload), fileName) :
					saver(payload); // everyone else can save dataURLs un-processed
			}

		} //end if dataURL passed?

		blob = payload instanceof myBlob ?
			payload :
			new myBlob([ payload ], {
				type : mimeType
			}) ;


		function dataUrlToBlob(strUrl) {
			var parts = strUrl.split(/[:;,]/),
				type = parts[1],
				decoder = parts[2] == "base64" ? atob : decodeURIComponent,
				binData = decoder(parts.pop()),
				mx = binData.length,
				i = 0,
				uiArr = new Uint8Array(mx);

			for (i; i < mx; ++i) uiArr[i] = binData.charCodeAt(i);

			return new myBlob([ uiArr ], {
				type : type
			});
		}

		function saver(url, winMode) {
			if ('download' in anchor) { //html5 A[download]
				anchor.href = url;
				anchor.setAttribute("download", fileName);
				anchor.className = "download-js-link";
				anchor.innerHTML = "downloading...";
				anchor.style.display = "none";
				document.body.appendChild(anchor);
				setTimeout(function() {
					anchor.click();
					document.body.removeChild(anchor);
					if (winMode === true) {
						setTimeout(function() {
							self.URL.revokeObjectURL(anchor.href);
						}, 250);
					}
				}, 66);
				return true;
			}

			// handle non-a[download] safari as best we can:
			if (/(Version)\/(\d+)\.(\d+)(?:\.(\d+))?.*Safari\//.test(navigator.userAgent)) {
				url = url.replace(/^data:([\w\/\-\+]+)/, defaultMime);
				if (!window.open(url)) { // popup blocked, offer direct download:
					if (confirm("Displaying New Document\n\nUse Save As... to download, then click back to return to this page.")) {
						location.href = url;
					}
				}
				return true;
			}

			//do iframe dataURL download (old ch+FF):
			var f = document.createElement("iframe");
			document.body.appendChild(f);

			if (!winMode) { // force a mime that will download:
				url = "data:" + url.replace(/^data:([\w\/\-\+]+)/, defaultMime);
			}
			f.src = url;
			setTimeout(function() {
				document.body.removeChild(f);
			}, 333);

		} //end saver




		if (navigator.msSaveBlob) { // IE10+ : (has Blob, but not a[download] or URL)
			return navigator.msSaveBlob(blob, fileName);
		}

		if (self.URL) { // simple fast and modern way using Blob and URL:
			saver(self.URL.createObjectURL(blob), true);
		} else {
			// handle non-Blob()+non-URL browsers:
			if (typeof blob === "string" || blob.constructor === toString) {
				try {
					return saver("data:" + mimeType + ";base64," + self.btoa(blob));
				} catch (y) {
					return saver("data:" + mimeType + "," + encodeURIComponent(blob));
				}
			}

			// Blob but not URL support:
			reader = new FileReader();
			reader.onload = function(e) {
				saver(this.result);
			};
			reader.readAsDataURL(blob);
		}
		return true;
	}; /* end download() */
}));
!function(e) {
	function t(i) {
		if (n[i]) return n[i].exports;
		var r = n[i] = {
			i : i,
			l : !1,
			exports : {}
		};
		return e[i].call(r.exports, r, r.exports, t), r.l = !0, r.exports
	}
	var n = {};
	t.m = e, t.c = n, t.i = function(e) {
		return e
	}, t.d = function(e, n, i) {
		t.o(e, n) || Object.defineProperty(e, n, {
			configurable : !1,
			enumerable : !0,
			get : i
		})
	}, t.n = function(e) {
		var n = e && e.__esModule ? function() {
			return e.default
		} : function() {
			return e
		};
		return t.d(n, "a", n), n
	}, t.o = function(e, t) {
		return Object.prototype.hasOwnProperty.call(e, t)
	}, t.p = "./", t(t.s = 10)
}([ function(e, t, n) {
	"use strict";
	var i = {
		isFirefox : function() {
			return "undefined" != typeof InstallTrigger
		},
		isIE : function() {
			return !!document.documentMode
		},
		isEdge : function() {
			return !i.isIE() && !!window.StyleMedia
		},
		isChrome : function() {
			return !!window.chrome && !!window.chrome.webstore
		},
		isSafari : function() {
			return Object.prototype.toString.call(window.HTMLElement).indexOf("Constructor") > 0
		}
	};
	t.a = i
}, function(e, t, n) {
	"use strict";
	function i(e, t) {
		if (e.focus(), o.a.isIE() || o.a.isEdge()) try {
				e.contentWindow.document.execCommand("print", !1, null)
			} catch (t) {
				e.contentWindow.print()
		} else e.contentWindow.print();
		t.showModal && a.a.close()
	}
	function r(e) {
		void 0 === e.print ? setTimeout(function() {
			r()
		}, 1e3) : (d.send(), setTimeout(function() {
			e.parentNode.removeChild(e)
		}, 2e3))
	}
	var o = n(0),
		a = n(3),
		d = {
			send : function(e, t) {
				document.getElementsByTagName("body")[0].appendChild(t);
				var n = document.getElementById(e.frameId);
				o.a.isIE() && "pdf" === e.type ? r(n) : t.onload = function() {
					if ("pdf" === e.type) i(n, e);else {
						var t = n.contentWindow || n.contentDocument;
						t.document && (t = t.document), t.body.innerHTML = e.htmlData, "image" === e.type ? t.getElementById("printableImage").onload = function() {
							i(n, e)
						} : i(n, e)
					}
				}
			}
		};
	t.a = d
}, function(e, t, n) {
	"use strict";
	function i(e, t) {
		return '<div style="font-family:' + t.font + " !important; font-size: " + t.font_size + ' !important; width:100%;">' + e + "</div>"
	}
	function r(e) {
		return e.charAt(0).toUpperCase() + e.slice(1)
	}
	function o(e, t) {
		var n = document.defaultView || window,
			i = [],
			r = "";
		if (n.getComputedStyle) {
			i = n.getComputedStyle(e, "");
			var o = [ "border", "float", "box", "break", "text-decoration" ],
				a = [ "clear", "display", "width", "min-width", "height", "min-height", "max-height" ];
			t.honorMarginPadding && o.push("margin", "padding"), t.honorColor && o.push("color");
			for (var d = 0; d < i.length; d++)
				for (var l = 0; l < a.length; l++) -1 === i[d].indexOf(o[l]) && 0 !== i[d].indexOf(a[l]) || (r += i[d] + ":" + i.getPropertyValue(i[d]) + ";")
		} else if (e.currentStyle) {
			i = e.currentStyle;
			for (var s in i) -1 !== i.indexOf("border") && -1 !== i.indexOf("color") && (r += s + ":" + i[s] + ";")
		}
		return r += "max-width: " + t.maxWidth + "px !important;" + t.font_size + " !important;"
	}
	function a(e, t) {
		for (var n = 0; n < e.length; n++) {
			var i = e[n],
				r = i.tagName;
			if ("INPUT" === r || "TEXTAREA" === r || "SELECT" === r) {
				var d = o(i, t),
					l = i.parentNode,
					s = "SELECT" === r ? document.createTextNode(i.options[i.selectedIndex].text) : document.createTextNode(i.value),
					c = document.createElement("div");
				c.appendChild(s), c.setAttribute("style", d), l.appendChild(c), l.removeChild(i)
			} else i.setAttribute("style", o(i, t));
			var p = i.children;
			p && p.length && a(p, t)
		}
	}
	function d(e, t) {
		var n = document.createElement("h1"),
			i = document.createTextNode(t);
		n.appendChild(i), n.setAttribute("style", "font-weight:300;"), e.insertBefore(n, e.childNodes[0])
	}
	t.a = i, t.b = r, t.c = o, t.d = a, t.e = d
}, function(e, t, n) {
	"use strict";
	var i = {
		show : function(e) {
			var t = document.createElement("div");
			t.setAttribute("style", "font-family:sans-serif; display:table; text-align:center; font-weight:300; font-size:30px; left:0; top:0;position:fixed; z-index: 9990;color: #0460B5; width: 100%; height: 100%; background-color:rgba(255,255,255,.9);transition: opacity .3s ease;"), t.setAttribute("id", "printJS-Modal");
			var n = document.createElement("div");
			n.setAttribute("style", "display:table-cell; vertical-align:middle; padding-bottom:100px;");
			var r = document.createElement("div");
			r.setAttribute("class", "printClose"), r.setAttribute("id", "printClose"), n.appendChild(r);
			var o = document.createElement("span");
			o.setAttribute("class", "printSpinner"), n.appendChild(o);
			var a = document.createTextNode(e.modalMessage);
			n.appendChild(a), t.appendChild(n), document.getElementsByTagName("body")[0].appendChild(t), document.getElementById("printClose").addEventListener("click", function() {
				i.close()
			})
		},
		close : function() {
			var e = document.getElementById("printJS-Modal");
			e.parentNode.removeChild(e)
		}
	};
	t.a = i
}, function(e, t, n) {
	"use strict";Object.defineProperty(t, "__esModule", {
		value : !0
	});
	var i = n(7),
		r = i.a.init;
	"undefined" != typeof window && (window.printJS = r), t.default = r
}, function(e, t, n) {
	"use strict";
	var i = n(2),
		r = n(1);
	t.a = {
		print : function(e, t) {
			var o = document.getElementById(e.printable);
			if (!o) return window.console.error("Invalid HTML element id: " + e.printable), !1;
			var a = document.createElement("div");
			a.appendChild(o.cloneNode(!0)), a.setAttribute("style", "display:none;"), a.setAttribute("id", "printJS-html"), o.parentNode.appendChild(a), a = document.getElementById("printJS-html"), a.setAttribute("style", n.i(i.c)(a, e) + "margin:0 !important;");
			var d = a.children;
			n.i(i.d)(d, e), e.header && n.i(i.e)(a, e.header), a.parentNode.removeChild(a), e.htmlData = n.i(i.a)(a.innerHTML, e), r.a.send(e, t)
		}
	}
}, function(e, t, n) {
	"use strict";
	var i = n(2),
		r = n(0),
		o = n(1);
	t.a = {
		print : function(e, t) {
			var a = document.createElement("img");
			a.src = e.printable, a.setAttribute("style", "width:100%;"), a.setAttribute("id", "printableImage");
			var d = document.createElement("div");
			if (d.setAttribute("style", "width:100%"), r.a.isFirefox()) {
				var l = document.createElement("canvas");
				l.setAttribute("width", a.width), l.setAttribute("height", a.height);l.getContext("2d").drawImage(a, 0, 0), a.setAttribute("src", l.toDataURL("JPEG", 1))
			}
			d.appendChild(a), e.header && n.i(i.e)(d), e.htmlData = d.outerHTML, o.a.send(e, t)
		}
	}
}, function(e, t, n) {
	"use strict";
	var i = n(0),
		r = n(3),
		o = n(9),
		a = n(5),
		d = n(6),
		l = n(8),
		s = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function(e) {
			return typeof e
		} : function(e) {
			return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
		},
		c = [ "pdf", "html", "image", "json" ],
		p = {
			printable : null,
			type : "pdf",
			header : null,
			maxWidth : 800,
			font : "TimesNewRoman",
			font_size : "12pt",
			honorMarginPadding : !0,
			honorColor : !1,
			properties : null,
			showModal : !1,
			modalMessage : "Retrieving Document...",
			frameId : "printJS",
			border : !0,
			htmlData : ""
		};
	t.a = {
		init : function() {
			var e = arguments[0];
			if (void 0 === e)
				throw new Error("printJS expects at least 1 attribute.");
			switch (
			void 0 === e ? "undefined" : s(e)) {
			case "string":
				p.printable = encodeURI(e), p.type = arguments[1] || p.type;
				break;case "object":
				p.printable = e.printable, p.type = e.type || p.type, p.frameId = e.frameId || p.frameId, p.header = void 0 !== e.header ? e.header : p.header, p.maxWidth = e.maxWidth || p.maxWidth, p.font = e.font || p.font, p.font_size = e.font_size || p.font_size, p.honorMarginPadding = void 0 !== e.honorMarginPadding ? e.honorMarginPadding : p.honorMarginPadding, p.properties = e.properties || p.properties, p.showModal = void 0 !== e.showModal ? e.showModal : p.showModal, p.modalMessage = void 0 !== e.modalMessage ? e.modalMessage : p.modalMessage;
				break;default:
				throw new Error('Unexpected argument type! Expected "string" or "object", got ' + (void 0 === e ? "undefined" : s(e)))
			}
			if (!p.printable)
				throw new Error("Missing printable information.");
			if (!p.type || "string" != typeof p.type || -1 === c.indexOf(p.type.toLowerCase()))
				throw new Error("Invalid print type. Available types are: pdf, html, image and json.");
			p.showModal && r.a.show(p);
			var t = document.getElementById(p.frameId);
			t && t.parentNode.removeChild(t);
			var n = void 0;
			switch (i.a.isIE() && "pdf" === p.type ? (n = document.createElement("embed"), n.setAttribute("type", "application/pdf"), n.setAttribute("style", "width:0px;height:0px;")) : (n = document.createElement("iframe"), n.setAttribute("style", "display:none;")), n.setAttribute("id", p.frameId), "pdf" !== p.type && (n.srcdoc = "<html><head></head><body></body></html>"), p.type) {
			case "pdf":
				if (i.a.isFirefox()) {
					window.open(p.printable, "_blank").focus(), p.showModal && r.a.close()
				} else o.a.print(p, n);
				break;case "image":
				d.a.print(p, n);
				break;case "html":
				a.a.print(p, n);
				break;case "json":
				l.a.print(p, n);
				break;default:
				throw new Error("Invalid print type. Available types are: pdf, html, image and json.")
			}
		}
	}
}, function(e, t, n) {
	"use strict";
	function i(e) {
		var t = e.printable,
			i = e.properties,
			o = '<div style="display:flex; flex-direction: column;">';
		o += '<div style="flex:1; display:flex;">';
		for (var a = 0; a < i.length; a++) o += '<div style="flex:1; padding:5px;">' + n.i(r.b)(i[a].displayName || i[a]) + "</div>";
		o += "</div>";
		for (var d = 0; d < t.length; d++) {
			o += '<div style="flex:1; display:flex;', o += e.border ? "border:1px solid lightgray;" : "", o += '">';
			for (var l = 0; l < i.length; l++) o += '<div style="flex:1; padding:5px;">' + t[d][i[l].field || i[l]] + "</div>";
			o += "</div>"
		}
		return o += "</div>"
	}
	var r = n(2),
		o = n(1),
		a = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function(e) {
			return typeof e
		} : function(e) {
			return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
		};
	t.a = {
		print : function(e, t) {
			if ("object" !== a(e.printable))
				throw new Error("Invalid javascript data object (JSON).");
			if (!e.properties || "object" !== a(e.properties))
				throw new Error("Invalid properties array for your JSON data.");
			var d = "";
			e.header && (d += '<h1 style="font-weight:300;">' + e.header + "</h1>"), d += i(e), e.htmlData = n.i(r.a)(d, e), o.a.send(e, t)
		}
	}
}, function(e, t, n) {
	"use strict";
	function i(e, t) {
		t.setAttribute("src", e.printable), o.a.send(e, t)
	}
	var r = n(0),
		o = n(1);
	t.a = {
		print : function(e, t) {
			if (e.showModal && !r.a.isIE()) {
				var n = document.createElement("img");
				n.src = e.printable;new Promise(function(e, t) {
					function i() {
						n.complete && (window.clearInterval(r), e("PrintJS: PDF loaded."))
					}
					var r = setInterval(i, 100)
				}).then(function(n) {
					i(e, t)
				})
			} else i(e, t)
		}
	}
}, function(e, t, n) {
	e.exports = n(4)
} ]);