import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GamificationEngineService {

  private gamificationUrl = 'http://localhost:8081/api'
  constructor(private http: HttpClient) {}

  private generateRuleFormData(name: string, repetitions: number, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, evaluableActionId: string, achievementAssignmentMessage: string, achievementAssignmentOnlyFirstTime: boolean, achievementAssignmentCondition: string, achievementAssignmentConditionParameters: any[], achievementAssignmentUnits: number, achievementAssignmentAssessmentLevel: string){
    const formData = new FormData();
    formData.append('name', name);
    formData.append('repetitions', String(repetitions));
    formData.append('gameSubjectAcronym', gameSubjectAcronym);
    formData.append('gameCourse', String(gameCourse));
    formData.append('gamePeriod', gamePeriod);
    formData.append('evaluableActionId', evaluableActionId);
    formData.append('achievementAssignmentMessage', achievementAssignmentMessage);
    formData.append('achievementAssignmentOnlyFirstTime', String(achievementAssignmentOnlyFirstTime));
    formData.append('achievementAssignmentCondition', achievementAssignmentCondition);
    formData.append('achievementAssignmentConditionParameters', JSON.stringify(achievementAssignmentConditionParameters));
    formData.append('achievementAssignmentUnits', String(achievementAssignmentUnits));
    formData.append('achievementAssignmentAssessmentLevel', achievementAssignmentAssessmentLevel);
    return formData;
  }

  private generateLeaderboardFormData(name: string, startDate: string, endDate: string, assessmentLevel: string, extent: string, anonymization: string, studentVisible: boolean, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, achievementId: number){
    const formData = new FormData();
    formData.append('name', name);
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);
    formData.append('assessmentLevel', assessmentLevel);
    formData.append('extent', extent);
    formData.append('anonymization', anonymization);
    formData.append('studentVisible', String(studentVisible));
    formData.append('gameSubjectAcronym', gameSubjectAcronym);
    formData.append('gameCourse', String(gameCourse));
    formData.append('gamePeriod', gamePeriod);
    formData.append('achievementId', String(achievementId));
    return formData;
  }

  postSimpleRule(name: string, repetitions: number, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, evaluableActionId: string, achievementId: number, achievementAssignmentMessage: string, achievementAssignmentOnlyFirstTime: boolean, achievementAssignmentCondition: string, achievementAssignmentConditionParameters: any[], achievementAssignmentUnits: number, achievementAssignmentAssessmentLevel: string){
    const formData = this.generateRuleFormData(name, repetitions, gameSubjectAcronym, gameCourse, gamePeriod, evaluableActionId, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentCondition, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevel);
    formData.append('achievementId', String(achievementId));
    return this.http.post(this.gamificationUrl + '/rules/simples', formData, {observe: 'response'});
  }

  postDateRule(name: string, repetitions: number, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, evaluableActionId: string, achievementId: number, achievementAssignmentMessage: string, achievementAssignmentOnlyFirstTime: boolean, achievementAssignmentCondition: string, achievementAssignmentConditionParameters: any[], achievementAssignmentUnits: number, achievementAssignmentAssessmentLevel: string, startDate: string, endDate: string){
    const formData = this.generateRuleFormData(name, repetitions, gameSubjectAcronym, gameCourse, gamePeriod, evaluableActionId, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentCondition, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevel);
    formData.append('achievementId', String(achievementId));
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);
    return this.http.post(this.gamificationUrl + '/rules/dates', formData, {observe: 'response'});
  }
  postAchievement(name: string, image: File, category: string){
    const formData = new FormData();
    formData.append('name', name);
    if (image) formData.append('icon', image);
    formData.append('category', category);
    return this.http.post(this.gamificationUrl + '/achievements', formData, {observe: 'response'});
  }

  getAchievements(){
    return this.http.get(this.gamificationUrl + '/achievements');
  }

  getEvaluableActions(){
    return this.http.get(this.gamificationUrl + '/evaluableActions');
  }

  getGames(){
    return this.http.get(this.gamificationUrl + '/games');
  }

  postGame(subjectAcronym: string, course: number, period: string, startDate: string, endDate: string, firstParameter: number, secondParameter: number, thirdParameter: number){
    const formData = new FormData();
    formData.append('subjectAcronym', subjectAcronym);
    formData.append('course', String(course));
    formData.append('period', period);
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);
    formData.append('firstLevelPolicyParameter', String(firstParameter));
    formData.append('secondLevelPolicyParameter', String(secondParameter));
    formData.append('thirdLevelPolicyParameter', String(thirdParameter));
    return this.http.post(this.gamificationUrl + '/games', formData, {observe: 'response'});
  }

  getSimpleRules(gameSubjectAcronym: string, gameCourse: string, gamePeriod: string){
    return this.http.get(this.gamificationUrl + '/rules/simples?gameSubjectAcronym=' + gameSubjectAcronym + '&gameCourse=' + gameCourse + '&gamePeriod=' + gamePeriod);
  }

  getDateRules(gameSubjectAcronym: string, gameCourse: string, gamePeriod: string){
    return this.http.get(this.gamificationUrl + '/rules/dates?gameSubjectAcronym=' + gameSubjectAcronym + '&gameCourse=' + gameCourse + '&gamePeriod=' + gamePeriod);
  }

  postLeaderboard(name: string, startDate: string, endDate: string, assessmentLevel: string, extent: string, anonymization: string, studentVisible: boolean, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, achievementId: number){
    const formData = this.generateLeaderboardFormData(name, startDate, endDate, assessmentLevel, extent, anonymization, studentVisible, gameSubjectAcronym, gameCourse, gamePeriod, achievementId);
    return this.http.post(this.gamificationUrl + '/leaderboards', formData, {observe: 'response'});
  }

  getSubjects(){
    return this.http.get(this.gamificationUrl + '/subjects');
  }

  postSubject(acronym: string, code: number, name: string, studies: string, school: string){
    const formData = new FormData();
    formData.append('acronym', acronym);
    formData.append('code', String(code));
    formData.append('name', name);
    formData.append('studies', studies);
    formData.append('school', school);
    return this.http.post(this.gamificationUrl + '/subjects', formData, {observe: 'response'})
  }

  postGameGroup(gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, group: number){
    const formData = new FormData();
    formData.append('gameSubjectAcronym', gameSubjectAcronym);
    formData.append('gameCourse', String(gameCourse));
    formData.append('gamePeriod', gamePeriod);
    formData.append('group', String(group));
    return this.http.post(this.gamificationUrl + '/gameGroups', formData, {observe: 'response'});
  }

  postOpenAI(evaluableActionDescription: string, condition: string, conditionParameters: any[]){
    const formData = new FormData();
    formData.append('evaluableActionDescription', evaluableActionDescription);
    formData.append('condition', condition);
    formData.append('conditionParameters', JSON.stringify(conditionParameters));
    return this.http.post(this.gamificationUrl + '/openAiApi', formData);
  }

  postImportData(gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, importFile: File){
    const formData = new FormData();
    formData.append('subjectAcronym', gameSubjectAcronym);
    formData.append('course', String(gameCourse));
    formData.append('period', gamePeriod);
    formData.append('groupNumber', '10');
    formData.append('importedData', importFile);
    return this.http.post(this.gamificationUrl + '/importData', formData);
  }

  getLeaderboards(gameSubjectAcronym: string, gameCourse: number, gamePeriod: string){
    return this.http.get(this.gamificationUrl + '/leaderboards?gameSubjectAcronym=' + gameSubjectAcronym + '&gameCourse=' + gameCourse + '&gamePeriod=' + gamePeriod);
  }

  deleteLeaderboard(leaderboardId: number){
    return this.http.delete(this.gamificationUrl + '/leaderboards/' + leaderboardId, { responseType: 'text' });
  }

  updateLeaderboard(leaderboardId: number, name: string, startDate: string, endDate: string, assessmentLevel: string, extent: string, anonymization: string, studentVisible: boolean, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, achievementId: number){
    const formData = this.generateLeaderboardFormData(name, startDate, endDate, assessmentLevel, extent, anonymization, studentVisible, gameSubjectAcronym, gameCourse, gamePeriod, achievementId);
    return this.http.put(this.gamificationUrl + '/leaderboards/' + leaderboardId, formData);
  }

  deleteSimpleRule(simpleRuleId: number){
    return this.http.delete(this.gamificationUrl + '/rules/simples/' + simpleRuleId, { responseType: 'text' });
  }

  deleteDateRule(dateRuleId: number){
    return this.http.delete(this.gamificationUrl + '/rules/dates/' + dateRuleId, { responseType: 'text' });
  }

  updateSimpleRule(simpleRuleId: number, name: string, repetitions: number, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, evaluableActionId: string, achievementAssignmentMessage: string, achievementAssignmentOnlyFirstTime: boolean, achievementAssignmentCondition: string, achievementAssignmentConditionParameters: any[], achievementAssignmentUnits: number, achievementAssignmentAssessmentLevel: string){
    const formData = this.generateRuleFormData(name, repetitions, gameSubjectAcronym, gameCourse, gamePeriod, evaluableActionId, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentCondition, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevel);
    return this.http.put(this.gamificationUrl + '/rules/simples/' + simpleRuleId, formData);
  }

  updateDateRule(dateRuleId: number, name: string, repetitions: number, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, evaluableActionId: string, achievementAssignmentMessage: string, achievementAssignmentOnlyFirstTime: boolean, achievementAssignmentCondition: string, achievementAssignmentConditionParameters: any[], achievementAssignmentUnits: number, achievementAssignmentAssessmentLevel: string, startDate: string, endDate: string){
    const formData = this.generateRuleFormData(name, repetitions, gameSubjectAcronym, gameCourse, gamePeriod, evaluableActionId, achievementAssignmentMessage, achievementAssignmentOnlyFirstTime, achievementAssignmentCondition, achievementAssignmentConditionParameters, achievementAssignmentUnits, achievementAssignmentAssessmentLevel);
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);
    return this.http.put(this.gamificationUrl + '/rules/dates/' + dateRuleId, formData);
  }

  updateGame(gameSubjectAcronym: string, gameCourse: number, gamePeriod: string, startDate: string, endDate: string){
    const formData = new FormData();
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);
    return this.http.put(this.gamificationUrl + '/games?gameSubjectAcronym=' + gameSubjectAcronym + '&gameCourse=' + gameCourse + '&gamePeriod=' + gamePeriod, formData);
  }
}
