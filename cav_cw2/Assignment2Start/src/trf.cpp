#include "trf.h"

cp::cp(float r, float g, float b, int visoValue){
	color = vec4(r, g, b, 1.0f);
	isoValue = visoValue;
}
cp::cp(float alpha, int visoValue){
	color = vec4(.0f, .0f, .0f, alpha);
	isoValue = visoValue;
}

cubic::cubic(void) {
	a = vec4::Zero();
	b = vec4::Zero();
	c = vec4::Zero();
	d = vec4::Zero();
}
// cubic::cubic(vec4 va, vec4 vb, vec4 vc, vec4 vd) : a(va), b(vb), c(vc), d(vd){}
// cubic::cubic(const vec4& va, const vec4& vb, const vec4& vc, const vec4& vd){
// 	 a = va;
// 	 b = vb;
// 	 c = vc;
// 	 d = vd;
// }
// cubic::cubic(const vec4& va, const vec4& vb, const vec4& vc, const vec4& vd) 
// : a(va.x, va.y, va.z, va.w), 
// b(vb.x, vb.y, vb.z, vb.w), 
// c(vc.x, vc.y, vc.z, vc.w), 
// d(vd.x, vc.y, vc.z, vc.w){}
cubic::cubic(const vec4& va, const vec4& vb, const vec4& vc, const vec4& vd) 
: a(va), b(vb), c(vc), d(vd){}

vec4 cubic::GetPointOnSpline(float s){
	return (((d * s) + c) * s + b) * s + a;
}

std::vector<cubic> cubic::CalculateCubicSpline(int n, std::vector<cp> v){
	std::vector<vec4> gamma(n + 1);
	std::vector<vec4> delta(n + 1);
	std::vector<vec4> D(n + 1);
	int i;

	float init_gamma = 1.0f / 2.0f;
	gamma.at(0) = vec4(init_gamma, init_gamma, init_gamma, init_gamma);
	for(i = 1; i < n; i++){
		gamma.at(i) = vec4::One() / ((vec4::One() * 4.0f) - gamma.at(i - 1));
	}
	gamma.at(n) = vec4::One() / ((vec4::One() * 2.0f) - gamma.at(n - 1));

	delta.at(0) = (v.at(1).color - v.at(0).color) * gamma.at(0) * 3.0f;
	for(i = 1; i < n; i++){
		delta.at(i) = ((v.at(i + 1).color - v.at(i - 1).color) * 3.0f - delta.at(i - 1)) * gamma.at(i);
	}
	delta.at(n) = ((v.at(n).color - v.at(n - 1).color) * 3.0f - delta.at(n - 1)) * gamma.at(n);

	D.at(n) = delta.at(n);
	for(i = n - 1; i >= 0; i--) {
		D.at(i) = delta.at(i) - gamma.at(i) * D.at(i + 1);
	}

	std::vector<cubic> C(n);
	for(i = 0; i < n; i++){
		C.at(i) = cubic(
			v.at(i).color, 
			D.at(i),
			(v.at(i + 1).color - v.at(i).color) * 3.0f - (D.at(i) - D.at(i + 1)) * 2.0f,
			(v.at(i).color - v.at(i + 1).color) * 2.0f + D.at(i) + D.at(i + 1)
			);
	}

	return C;
}