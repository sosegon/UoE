#pragma once

#include "vec.h"
#include <vector>

// Control point for the transfer function
class cp {
public:
	vec4 color;
	int isoValue;
	cp(float r, float g, float b, int isoValue);
	cp(float alpha, int isoValue);
};

class cubic {
public:
	vec4 a, b, c, d;
	cubic(void);
	cubic(const vec4& a, const vec4& b, const vec4& c, const vec4& d);

	vec4 GetPointOnSpline(float s);
	static std::vector<cubic> CalculateCubicSpline(int n, std::vector<cp> controlPoints);

};
