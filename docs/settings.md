# Documentation Backend - Module Paramètres
## Vue d'ensemble
Ce document décrit les structures de données, les attributs et les APIs pour le module **Paramètres** de l'application Cherif Consulting. Ce module gère la configuration globale de l'application.
---
## Table des matières
1. [Catégories de Documents](#1-catégories-de-documents)
2. [Types de Projets](#2-types-de-projets)
3. [Types de Contrats](#3-types-de-contrats)
4. [Statuts de Projet](#4-statuts-de-projet)
5. [Priorités](#5-priorités)
6. [Étiquettes (Tags)](#6-étiquettes-tags)
7. [Rôles et Permissions](#7-rôles-et-permissions)
8. [Paramètres Généraux](#8-paramètres-généraux)
9. [Paramètres de Notification](#9-paramètres-de-notification)
---
## 1. Catégories de Documents
### Description
Les catégories de documents permettent de classifier les fichiers associés aux clients, projets et factures.
### Interface TypeScript
```typescript
interface DocumentCategory {
  id: string;          // Identifiant unique (UUID recommandé)
  name: string;        // Nom affiché (ex: "Contrat", "Proposition")
  key: string;         // Clé technique unique (ex: "contract", "proposal")
  color: string;       // Classes CSS pour le style (ex: "bg-primary/20 text-primary")
}
```
### Attributs
| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `id` | `string` | Oui | Identifiant unique généré automatiquement |
| `name` | `string` | Oui | Libellé affiché dans l'interface (2-50 caractères) |
| `key` | `string` | Oui | Clé technique en snake_case ou kebab-case |
| `color` | `string` | Oui | Classes Tailwind CSS pour le badge |
### Valeurs par défaut
| ID | Nom | Clé | Couleur |
|----|-----|-----|---------|
| `dc1` | Contrat | `contract` | `bg-health-green/20 text-health-green` |
| `dc2` | Cahier des charges | `sow` | `bg-primary/20 text-primary` |
| `dc3` | Proposition | `proposal` | `bg-health-amber/20 text-health-amber` |
| `dc4` | Spécifications | `requirements` | `bg-purple-500/20 text-purple-400` |
| `dc5` | Design | `design` | `bg-pink-500/20 text-pink-400` |
| `dc6` | Rapport de test | `test-report` | `bg-blue-500/20 text-blue-400` |
| `dc7` | Facture | `invoice` | `bg-health-red/20 text-health-red` |
| `dc8` | Compte-rendu | `meeting-notes` | `bg-muted text-muted-foreground` |
### Options de couleur disponibles
| Valeur CSS | Label |
|------------|-------|
| `bg-primary/20 text-primary` | Bleu |
| `bg-health-green/20 text-health-green` | Vert |
| `bg-health-amber/20 text-health-amber` | Jaune |
| `bg-health-red/20 text-health-red` | Rouge |
| `bg-purple-500/20 text-purple-400` | Violet |
| `bg-pink-500/20 text-pink-400` | Rose |
| `bg-blue-500/20 text-blue-400` | Bleu clair |
| `bg-muted text-muted-foreground` | Gris |
### Opérations CRUD
```typescript
// CREATE
addDocumentCategory(category: Omit<DocumentCategory, 'id'>): void
// UPDATE
updateDocumentCategory(id: string, category: Partial<DocumentCategory>): void
// DELETE
deleteDocumentCategory(id: string): void
```
### Exemple de payload (création)
```json
{
  "name": "Rapport technique",
  "key": "technical-report",
  "color": "bg-blue-500/20 text-blue-400"
}
```
---
## 2. Types de Projets
### Description
Définit les différents modèles de facturation et d'engagement pour les projets.
### Interface TypeScript
```typescript
interface ProjectTypeConfig {
  id: string;          // Identifiant unique
  name: string;        // Nom affiché
  key: string;         // Clé technique (utilisée dans Project.type)
  description: string; // Description du type
}
```
### Attributs
| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `id` | `string` | Oui | Identifiant unique |
| `name` | `string` | Oui | Libellé affiché (ex: "Prix fixe") |
| `key` | `string` | Oui | Clé technique lowercase (ex: "fixed", "tm") |
| `description` | `string` | Oui | Description explicative |
### Valeurs par défaut
| ID | Nom | Clé | Description |
|----|-----|-----|-------------|
| `pt1` | Prix fixe | `fixed` | Projet au forfait avec budget défini |
| `pt2` | Régie | `tm` | Facturation au temps passé |
| `pt3` | Forfait mensuel | `retainer` | Abonnement mensuel récurrent |
### Opérations CRUD
```typescript
// CREATE
addProjectType(projectType: Omit<ProjectTypeConfig, 'id'>): void
// UPDATE
updateProjectType(id: string, projectType: Partial<ProjectTypeConfig>): void
// DELETE
deleteProjectType(id: string): void
```
### Exemple de payload
```json
{
  "name": "Assistance technique",
  "key": "support",
  "description": "Contrat de maintenance et support technique"
}
```
---
## 3. Types de Contrats
### Description
Définit les types de relations contractuelles avec les clients.
### Interface TypeScript
```typescript
interface ContractTypeConfig {
  id: string;          // Identifiant unique
  name: string;        // Nom affiché
  key: string;         // Clé technique
  description: string; // Description du contrat
}
```
### Attributs
| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `id` | `string` | Oui | Identifiant unique |
| `name` | `string` | Oui | Libellé affiché (ex: "Enterprise") |
| `key` | `string` | Oui | Clé technique lowercase |
| `description` | `string` | Oui | Description du type de contrat |
### Valeurs par défaut
| ID | Nom | Clé | Description |
|----|-----|-----|-------------|
| `ct1` | Enterprise | `enterprise` | Contrat entreprise avec SLA premium |
| `ct2` | Retainer | `retainer` | Forfait mensuel récurrent |
| `ct3` | Project-Based | `project` | Contrat par projet individuel |
| `ct4` | Consulting | `consulting` | Mission de conseil ponctuelle |
### Opérations CRUD
```typescript
// CREATE
addContractType(contractType: Omit<ContractTypeConfig, 'id'>): void
// UPDATE
updateContractType(id: string, contractType: Partial<ContractTypeConfig>): void
// DELETE
deleteContractType(id: string): void
```
### Exemple de payload
```json
{
  "name": "Partenariat",
  "key": "partnership",
  "description": "Accord de partenariat long terme"
}
```
---
## 4. Statuts de Projet
### Description
États du cycle de vie d'un projet. Ces statuts sont actuellement en lecture seule.
### Type TypeScript
```typescript
type ProjectStatus = 
  | 'draft'      // Brouillon
  | 'discovery'  // Découverte
  | 'approved'   // Approuvé
  | 'delivery'   // Livraison
  | 'review'     // Revue
  | 'delivered'  // Livré
  | 'closed'     // Clôturé
  | 'on-hold'    // En pause
  | 'cancelled'; // Annulé
```
### Configuration des statuts
| ID | Nom | Couleur CSS | Catégorie |
|----|-----|-------------|-----------|
| 1 | Brouillon | `bg-muted` | project |
| 2 | Découverte | `bg-primary` | project |
| 3 | Approuvé | `bg-health-green` | project |
| 4 | Livraison | `bg-primary` | project |
| 5 | Revue | `bg-health-amber` | project |
| 6 | Livré | `bg-health-green` | project |
| 7 | Clôturé | `bg-muted` | project |
---
## 5. Priorités
### Description
Niveaux de priorité pour les tâches et user stories.
### Type TypeScript
```typescript
type Priority = 'must' | 'should' | 'could';
```
### Configuration
| ID | Nom | Clé | Couleur CSS |
|----|-----|-----|-------------|
| 1 | Doit avoir | `must` | `bg-health-red` |
| 2 | Devrait avoir | `should` | `bg-health-amber` |
| 3 | Pourrait avoir | `could` | `bg-primary` |
---
## 6. Étiquettes (Tags)
### Description
Tags personnalisables pour catégoriser clients et projets.
### Configuration par défaut
| ID | Nom | Couleur CSS |
|----|-----|-------------|
| 1 | entreprise | `text-primary` |
| 2 | tech | `text-blue-400` |
| 3 | stratégique | `text-health-green` |
| 4 | finance | `text-health-amber` |
| 5 | santé | `text-pink-400` |
| 6 | conformité | `text-purple-400` |
### Utilisation
Les tags sont stockés comme tableau de strings dans l'entité Client :
```typescript
interface Client {
  // ... autres champs
  tags: string[];  // ex: ['enterprise', 'tech', 'stratégique']
}
```
---
## 7. Rôles et Permissions
### Description
Système RBAC (Role-Based Access Control) pour gérer les accès utilisateurs.
### Type Enum (Base de données)
```sql
CREATE TYPE app_role AS ENUM ('admin', 'moderator', 'user');
```
### Configuration des rôles
| ID | Nom | Description | Permissions |
|----|-----|-------------|-------------|
| 1 | Admin | Accès complet à toutes les fonctionnalités | CRUD sur toutes les entités |
| 2 | Collaborateur interne | Peut voir/modifier les clients et projets assignés | Lecture/écriture limitée |
| 3 | Viewer client | Accès en lecture seule à leurs projets | Lecture seule |
### Table user_roles (Supabase)
```sql
CREATE TABLE public.user_roles (
  id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
  user_id UUID NOT NULL,
  role app_role NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
```
### Fonctions de vérification
```sql
-- Vérifie si un utilisateur a un rôle spécifique
CREATE FUNCTION public.has_role(_user_id UUID, _role app_role)
RETURNS BOOLEAN AS $$
  SELECT EXISTS (
    SELECT 1 FROM public.user_roles
    WHERE user_id = _user_id AND role = _role
  )
$$ LANGUAGE sql STABLE SECURITY DEFINER;
-- Vérifie si l'utilisateur courant est admin
CREATE FUNCTION public.is_admin()
RETURNS BOOLEAN AS $$
  SELECT public.has_role(auth.uid(), 'admin')
$$ LANGUAGE sql STABLE SECURITY DEFINER;
```
### Politiques RLS
```sql
-- Les utilisateurs peuvent voir leurs propres rôles
CREATE POLICY "Users can view their own roles" ON public.user_roles
FOR SELECT USING (auth.uid() = user_id);
-- Les admins peuvent gérer tous les rôles
CREATE POLICY "Admins can manage roles" ON public.user_roles
FOR ALL USING (
  EXISTS (
    SELECT 1 FROM user_roles
    WHERE user_id = auth.uid() AND role = 'admin'
  )
);
```
---
## 8. Paramètres Généraux
### Description
Configuration globale de l'entreprise et de l'application.
### Interface (à implémenter)
```typescript
interface GeneralSettings {
  companyName: string;        // Nom de l'entreprise
  email: string;              // Email de contact
  timezone: string;           // Fuseau horaire (ex: "Europe/Paris")
  defaultCurrency: string;    // Devise par défaut (ex: "EUR")
}
```
### Attributs
| Champ | Type | Requis | Défaut | Description |
|-------|------|--------|--------|-------------|
| `companyName` | `string` | Oui | "Cherif Consulting" | Nom de l'entreprise |
| `email` | `string` | Oui | - | Email principal de contact |
| `timezone` | `string` | Oui | "Europe/Paris" | Fuseau horaire IANA |
| `defaultCurrency` | `string` | Oui | "EUR" | Code ISO 4217 |
---
## 9. Paramètres de Notification
### Description
Configuration des alertes et rappels automatiques.
### Interface (à implémenter)
```typescript
interface NotificationSettings {
  emailNotifications: boolean;      // Notifications par email
  slackIntegration: boolean;        // Intégration Slack
  milestoneReminders: boolean;      // Rappels jalons
  invoiceDueAlerts: boolean;        // Alertes échéances factures
  weeklyDigest: boolean;            // Résumé hebdomadaire
}
```
### Attributs
| Champ | Type | Défaut | Description |
|-------|------|--------|-------------|
| `emailNotifications` | `boolean` | `true` | Activer les emails |
| `slackIntegration` | `boolean` | `false` | Poster dans Slack |
| `milestoneReminders` | `boolean` | `true` | Rappel 3 jours avant échéance |
| `invoiceDueAlerts` | `boolean` | `true` | Alerte factures en retard |
| `weeklyDigest` | `boolean` | `true` | Email résumé chaque lundi |
---
## Schéma de base de données recommandé
### Table: settings_document_categories
```sql
CREATE TABLE public.settings_document_categories (
  id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
  name TEXT NOT NULL,
  key TEXT NOT NULL UNIQUE,
  color TEXT NOT NULL DEFAULT 'bg-primary/20 text-primary',
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
ALTER TABLE public.settings_document_categories ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Admin users can manage document categories"
ON public.settings_document_categories
FOR ALL USING (is_admin());
```
### Table: settings_project_types
```sql
CREATE TABLE public.settings_project_types (
  id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
  name TEXT NOT NULL,
  key TEXT NOT NULL UNIQUE,
  description TEXT NOT NULL DEFAULT '',
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
ALTER TABLE public.settings_project_types ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Admin users can manage project types"
ON public.settings_project_types
FOR ALL USING (is_admin());
```
### Table: settings_contract_types
```sql
CREATE TABLE public.settings_contract_types (
  id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
  name TEXT NOT NULL,
  key TEXT NOT NULL UNIQUE,
  description TEXT NOT NULL DEFAULT '',
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
ALTER TABLE public.settings_contract_types ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Admin users can manage contract types"
ON public.settings_contract_types
FOR ALL USING (is_admin());
```
### Table: settings_general
```sql
CREATE TABLE public.settings_general (
  id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
  company_name TEXT NOT NULL DEFAULT 'Cherif Consulting',
  email TEXT NOT NULL,
  timezone TEXT NOT NULL DEFAULT 'Europe/Paris',
  default_currency TEXT NOT NULL DEFAULT 'EUR',
  dark_mode BOOLEAN NOT NULL DEFAULT true,
  compact_mode BOOLEAN NOT NULL DEFAULT false,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
ALTER TABLE public.settings_general ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Admin users can manage general settings"
ON public.settings_general
FOR ALL USING (is_admin());
```
---
## API Endpoints
### Endpoint unique
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/settings` | Récupérer toutes les configurations |
| PATCH | `/api/v1/settings` | Mettre à jour une ou plusieurs sections |

### Exemple de réponse
```json
{
  "documentCategories": [],
  "projectTypes": [],
  "contractTypes": [],
  "general": {
    "companyName": "Cherif Consulting",
    "email": "contact@example.com",
    "timezone": "Europe/Paris",
    "defaultCurrency": "EUR"
  },
  "notifications": {
    "emailNotifications": true,
    "slackIntegration": false,
    "milestoneReminders": true,
    "invoiceDueAlerts": true,
    "weeklyDigest": true
  }
}
```

### Exemple de payload (PATCH)
```json
{
  "documentCategories": [
    {
      "name": "Rapport technique",
      "key": "technical-report",
      "color": "bg-blue-500/20 text-blue-400"
    }
  ],
  "documentCategoryDeletes": ["dc1"],
  "projectTypes": [
    {
      "id": "pt1",
      "description": "Projet au forfait avec budget défini"
    }
  ],
  "contractTypes": [
    {
      "name": "Partenariat",
      "key": "partnership",
      "description": "Accord de partenariat long terme"
    }
  ],
  "general": {
    "companyName": "Cherif Consulting",
    "email": "contact@example.com",
    "timezone": "Europe/Paris",
    "defaultCurrency": "EUR"
  },
  "notifications": {
    "emailNotifications": true,
    "slackIntegration": false,
    "milestoneReminders": true,
    "invoiceDueAlerts": true,
    "weeklyDigest": true
  }
}
```
---
## Notes d'implémentation
1. **Validation** : Toutes les clés (`key`) doivent être uniques et en format snake_case ou kebab-case
2. **Suppression** : Vérifier qu'aucune entité n'utilise la catégorie/type avant suppression
3. **Cache** : Ces données sont peu volatiles, implémenter un cache côté client
4. **Migration** : Lors de la migration vers la base de données, insérer les valeurs par défaut
5. **Audit** : Logger toutes les modifications pour traçabilité
---
## Changelog
| Version | Date | Auteur | Changements |
|---------|------|--------|-------------|
| 1.0.0 | 2026-02-05 | Système | Documentation initiale |
