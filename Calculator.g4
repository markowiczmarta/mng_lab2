grammar Calculator;

expression: multiplyingExpression ((PLUS | MINUS) multiplyingExpression)*;
integralExpression: MINUS INT | INT;
multiplyingExpression: powerExpression ((MULT | DIV) powerExpression)*;
powerExpression: sqrtExpression | integralExpression(POW powerExpression)*;
sqrtExpression: SQRT INT;

INT: [0-9]+ ;
PLUS: '+' ;
MINUS: '-' ;
MULT: '*';
DIV: '/';
SQRT: 'sqrt';
POW:'^';
INTEGRAL: 'cal';
WS : [ \t\r\n]+ -> skip ;
