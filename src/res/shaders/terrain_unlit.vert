// texture vertex shader
#version 330 core

layout(location = 0) in vec4 VertexIn;
layout(location = 1) in vec2 ShadeIn;
layout(location = 2) in vec2 TexIn;

layout(location = 3) in vec3 NormalIn1;
layout(location = 4) in vec3 NormalIn2;
layout(location = 5) in vec3 NormalIn3;


/*noperspective*/ out vec2 texCoord, shadeCoord;
out vec3 vertexPos;

uniform mat4 projectionMatrix = mat4(1.0);
uniform mat4 viewMatrix  = mat4(1.0);
uniform mat4 modelMatrix  = mat4(1.0);

void main()
{
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * VertexIn;
	//gl_Position.xy = floor(gl_Position.xy*60)/60;

	texCoord = TexIn;
	shadeCoord = ShadeIn;
	vertexPos = vec3(modelMatrix * VertexIn);
}
