#define ORDRE_DES_ETAPES_MACRO ORDRE_DES_ETAPES
#define STEPS_ORDER_MACRO STEPS_ORDER


#define	MODELE_FIGE(Condition, DEF, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	SI (Condition) IL_PEUT_SE_PRODUIRE \
		DEFAILLANCE DEF LOI EXP(0.1) PROVOQUE DEF<--VRAI
#define	MODEL_FROZEN(Condition, DEF, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	IF (Condition) MAY_OCCUR \
		FAULT DEF DIST EXP(0.1) INDUCING DEF<--TRUE
		
#define MODELE_GLM(Condition, DEF, gamma, lambda, mu, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	SI (Condition) IL_PEUT_SE_PRODUIRE \
		DEFAILLANCE DEF LOI EXP(0.1) PROVOQUE DEF<--VRAI
#define MODEL_GLM(Condition, DEF, gamma, lambda, mu, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	IF (Condition) MAY_OCCUR \
		FAULT DEF DIST EXP(0.1) INDUCING DEF<--TRUE		
		
#define MODELE_GLTM(Condition, DEF, gamma, lambda, tm, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	SI (Condition) IL_PEUT_SE_PRODUIRE \
		DEFAILLANCE DEF LOI EXP(0.1) PROVOQUE DEF<--VRAI
#define MODEL_GLMT(Condition, DEF, gamma, lambda, tm, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	IF (Condition) MAY_OCCUR \
		FAULT DEF DIST EXP(0.1) INDUCING DEF<--TRUE

#define MODELE_G(Condition, DEF, gamma, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	SI (Condition) IL_PEUT_SE_PRODUIRE \
		DEFAILLANCE DEF LOI EXP(0.1) PROVOQUE DEF<--VRAI
#define MODEL_G(Condition, DEF, gamma, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	IF (Condition) MAY_OCCUR \
		FAULT DEF DIST EXP(0.1) INDUCING DEF<--TRUE
		
#define MODELE_TA(Condition, DEF, gamma, lambda, mu, lambda_test, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	SI (Condition) IL_PEUT_SE_PRODUIRE \
		DEFAILLANCE DEF LOI EXP(0.1) PROVOQUE DEF<--VRAI
#define MODEL_RT(Condition, DEF, gamma, lambda, mu, lambda_test, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	IF (Condition) MAY_OCCUR \
		FAULT DEF DIST EXP(0.1) INDUCING DEF<--TRUE

#define MODELE_TPE(Condition, DEF, gamma, lambda, mu, gamma_test, gamma_reconfig, gamma_non_detect, tau_avant_test, tau_inter_test, tau_test, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	SI (Condition) IL_PEUT_SE_PRODUIRE \
		DEFAILLANCE DEF LOI EXP(0.1) PROVOQUE DEF<--VRAI
#define MODEL_PET(Condition, DEF, gamma, lambda, mu, gamma_test, gamma_reconfig, gamma_non_detect, tau_avant_test, tau_inter_test, tau_test, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	IF (Condition) MAY_OCCUR \
		FAULT DEF DIST EXP(0.1) INDUCING DEF<--TRUE

#define MODELE_F(Condition, DEF, frequence, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	SI (Condition) IL_PEUT_SE_PRODUIRE \
		DEFAILLANCE DEF LOI EXP(0.1) PROVOQUE DEF<--VRAI
#define MODEL_F(Condition, DEF, frequence, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	IF (Condition) MAY_OCCUR \
		FAULT DEF DIST EXP(0.1) INDUCING DEF<--TRUE

#define MODELE_WB(Condition, DEF, alpha, beta, t0, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	SI (Condition) IL_PEUT_SE_PRODUIRE \
		DEFAILLANCE DEF LOI EXP(0.1) PROVOQUE DEF<--VRAI
#define MODEL_WB(Condition, DEF, alpha, beta, t0, groupes) ; \
OCCURRENCE \
	DEF \
	groupes \
	IF (Condition) MAY_OCCUR \
		FAULT DEF DIST EXP(0.1) INDUCING DEF<--TRUE

