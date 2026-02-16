# Reptile Management System - Implementation TODO

> **Last Updated:** February 16, 2026
> **Status:** In Progress
> **Total Tasks:** 20

---

## 📊 Project Status Overview

### ✅ Completed Foundation
- [x] Backend infrastructure (Spring Boot with Spring Security)
- [x] Frontend base (Angular 21 with Tailwind CSS)
- [x] User authentication and authorization (JWT)
- [x] Reptile CRUD operations
- [x] Image upload and management
- [x] Basic feeding, weight, and shedding log entities
- [x] Docker containerization setup
- [x] Poop/defecation log (backend entity, DTO, repository, mapper, service, controller)
- [x] Poop/defecation log frontend model and service methods

### 🎯 Current Implementation Focus
The following features need to be implemented to complete the vision described in the README.

---

## Phase 2: Data Visualization & Statistics (4 tasks)

### 🟡 Task 6: Install Chart.js Library for Frontend Statistics
**Priority:** High  
**Estimated Time:** 30 minutes

- [ ] Install Chart.js: `pnpm add chart.js ng2-charts`
- [ ] Install date-fns for date manipulation: `pnpm add date-fns`
- [ ] Configure Chart.js in Angular app
- [ ] Add type definitions if needed

**Commands:**
```bash
cd frontend
pnpm add chart.js ng2-charts date-fns
```

---

### 🟡 Task 7: Create Weight Statistics Chart Component
**Priority:** High  
**Estimated Time:** 3 hours

- [ ] Create standalone chart component for weight tracking
- [ ] Implement line chart showing weight over time
- [ ] Add trend line calculation
- [ ] Display growth rate (percentage change)
- [ ] Show weight gain/loss indicators
- [ ] Add date range selector (1 month, 3 months, 6 months, 1 year, all)
- [ ] Implement responsive design

**Features:**
- Line chart with data points
- Hover tooltips showing exact weight and date
- Color-coded sections (gaining, stable, losing weight)
- Statistical summary (min, max, average, current)

**Files to create:**
- `frontend/src/app/features/reptile-management/components/weight-chart/weight-chart.component.ts`
- `frontend/src/app/features/reptile-management/components/weight-chart/weight-chart.component.html`
- `frontend/src/app/features/reptile-management/components/weight-chart/weight-chart.component.css`

---

### 🟡 Task 8: Implement Feeding Frequency Chart Component
**Priority:** Medium  
**Estimated Time:** 2 hours

- [ ] Create chart showing feeding frequency over time
- [ ] Display bar chart (feedings per week/month)
- [ ] Show feeding intervals (days between feedings)
- [ ] Highlight missed feeding schedules
- [ ] Add food type distribution (pie chart)

**Files to create:**
- `frontend/src/app/features/reptile-management/components/feeding-chart/feeding-chart.component.ts`
- `frontend/src/app/features/reptile-management/components/feeding-chart/feeding-chart.component.html`
- `frontend/src/app/features/reptile-management/components/feeding-chart/feeding-chart.component.css`

---

### 🟡 Task 9: Add Growth Rate Calculation and Visualization
**Priority:** Medium  
**Estimated Time:** 2 hours

- [ ] Calculate growth rate from weight logs
- [ ] Create visualization showing:
  - Weight gain per month
  - Percentage growth rate
  - Growth velocity chart
  - Expected vs actual growth (if reference data available)
- [ ] Add statistics card with key metrics

**Integration:** This should be part of the weight chart component or a separate analytics section.

---

## Phase 3: Complete Log Management UI (5 tasks)

### 🟢 Task 10: Create Comprehensive Logs Tabs in Reptile Detail View
**Priority:** High  
**Estimated Time:** 2 hours

- [ ] Refactor reptile detail page to use tabbed interface
- [ ] Create tabs: Overview, Feeding, Weight, Shedding, Poop, Photos
- [ ] Implement smooth tab switching with routing
- [ ] Style tabs with DaisyUI components
- [ ] Preserve tab state in URL (query params)

**Files to modify:**
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.html`
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.ts`
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.css`

---

### 🟢 Task 11: Implement Feeding Log CRUD Forms and Display
**Priority:** High  
**Estimated Time:** 3 hours

- [ ] Create feeding log table/list view
- [ ] Add "Log Feeding" modal with form:
  - Date/time picker
  - Food type dropdown (with custom option)
  - Quantity input
  - Notes textarea
- [ ] Implement edit functionality
- [ ] Implement delete with confirmation
- [ ] Sort logs by date (newest first)
- [ ] Add pagination if more than 20 logs
- [ ] Show feeding frequency statistics

**Files to modify:**
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.html`
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.ts`

---

### 🟢 Task 12: Implement Weight Log CRUD Forms and Display
**Priority:** High  
**Estimated Time:** 3 hours

- [ ] Create weight log table/list view with chart
- [ ] Add "Log Weight" modal with form:
  - Date picker
  - Weight input (grams)
  - Unit selector (g, kg, oz, lb)
  - Notes textarea
- [ ] Implement edit functionality
- [ ] Implement delete with confirmation
- [ ] Display weight chart (from Task 7)
- [ ] Show weight change indicators (+/- from last measurement)
- [ ] Calculate and display statistics

**Files to modify:**
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.html`
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.ts`

---

### 🟢 Task 13: Implement Shedding Log CRUD Forms and Display
**Priority:** Medium  
**Estimated Time:** 2.5 hours

- [ ] Create shedding log table/list view
- [ ] Add "Log Shed" modal with form:
  - Date picker
  - Quality selector (Complete, Partial, Incomplete)
  - Notes textarea (for issues)
- [ ] Implement edit functionality
- [ ] Implement delete with confirmation
- [ ] Show shedding cycle statistics (average days between sheds)
- [ ] Highlight problematic sheds

**Files to modify:**
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.html`
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.ts`

---

### 🟢 Task 14: Implement Poop Log CRUD Forms and Display
**Priority:** Medium  
**Estimated Time:** 2.5 hours

- [ ] Create poop log table/list view
- [ ] Add "Log Defecation" modal with form:
  - Date/time picker
  - Consistency selector (Normal, Runny, Hard, Watery)
  - Color input
  - Parasites checkbox
  - Notes textarea
- [ ] Implement edit functionality
- [ ] Implement delete with confirmation
- [ ] Show defecation frequency statistics
- [ ] Highlight abnormal entries (parasites, unusual consistency)

**Files to modify:**
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.html`
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.ts`

---

## Phase 4: Dashboard Overhaul (6 tasks)

### 🔵 Task 15: Rework Dashboard - Add Statistics Widgets
**Priority:** High  
**Estimated Time:** 3 hours

- [ ] Create new dashboard layout with widget grid
- [ ] Add statistics cards:
  - Total reptiles in collection
  - Active reptiles count
  - Reptiles in quarantine
  - Average collection age
- [ ] Add visual indicators (icons, colors)
- [ ] Make widgets clickable (navigate to filtered views)
- [ ] Implement responsive grid layout

**Files to modify:**
- `frontend/src/app/features/dashboard/dashboard/dashboard.html`
- `frontend/src/app/features/dashboard/dashboard/dashboard.ts`
- `frontend/src/app/features/dashboard/dashboard/dashboard.css`

---

### 🔵 Task 16: Add Upcoming Feeding Schedule Widget to Dashboard
**Priority:** High  
**Estimated Time:** 2 hours

- [ ] Create "Upcoming Feedings" widget
- [ ] Calculate which reptiles need feeding based on:
  - Last fed date
  - Species-specific feeding intervals
- [ ] Display list of reptiles due for feeding today/this week
- [ ] Sort by urgency (overdue first)
- [ ] Add quick "Mark as Fed" action button
- [ ] Show count badge (X reptiles need feeding)

**Files to modify:**
- `frontend/src/app/features/dashboard/dashboard/dashboard.html`
- `frontend/src/app/features/dashboard/dashboard/dashboard.ts`

---

### 🔵 Task 17: Create Recent Weight Changes Widget for Dashboard
**Priority:** Medium  
**Estimated Time:** 2 hours

- [ ] Create "Weight Tracking" widget
- [ ] Show reptiles with recent weight changes
- [ ] Display trend indicators (↑ gaining, ↓ losing, → stable)
- [ ] Highlight significant changes (>5% in 30 days)
- [ ] Show mini sparkline charts for each reptile
- [ ] Add "View Details" link to reptile page

**Files to modify:**
- `frontend/src/app/features/dashboard/dashboard/dashboard.html`
- `frontend/src/app/features/dashboard/dashboard/dashboard.ts`

---

### 🔵 Task 18: Add Health Alerts Widget (Overdue Feeding/Shedding Issues)
**Priority:** High  
**Estimated Time:** 2.5 hours

- [ ] Create "Health Alerts" widget
- [ ] Implement alert rules:
  - Feeding overdue (no feeding in X days based on species)
  - Weight loss (>5% decrease)
  - No shed in expected timeframe
  - No poop in extended period
  - Incomplete shed quality
- [ ] Display alerts with severity levels (warning, danger)
- [ ] Add dismiss/snooze functionality
- [ ] Link directly to affected reptile

**Files to modify:**
- `frontend/src/app/features/dashboard/dashboard/dashboard.html`
- `frontend/src/app/features/dashboard/dashboard/dashboard.ts`

---

### 🔵 Task 19: Implement Reptile Collection Overview Cards on Dashboard
**Priority:** Medium  
**Estimated Time:** 2 hours

- [ ] Create grid of reptile cards on dashboard
- [ ] Show highlight image for each reptile
- [ ] Display key info (name, species, last fed)
- [ ] Add status badges (Active, Quarantine, etc.)
- [ ] Implement quick actions menu per card
- [ ] Add filtering and sorting options
- [ ] Make cards clickable to view details

**Files to modify:**
- `frontend/src/app/features/dashboard/dashboard/dashboard.html`
- `frontend/src/app/features/dashboard/dashboard/dashboard.ts`

---

### 🔵 Task 20: Add Quick Action Buttons to Dashboard (Feed, Weigh, Log)
**Priority:** Medium  
**Estimated Time:** 2 hours

- [ ] Create floating action button (FAB) or quick action bar
- [ ] Add actions:
  - Quick feed (select reptile, log feeding)
  - Quick weigh (select reptile, enter weight)
  - Add new reptile
  - Bulk actions (feed all, etc.)
- [ ] Implement modal forms for each action
- [ ] Update dashboard after action completion
- [ ] Add keyboard shortcuts

**Files to modify:**
- `frontend/src/app/features/dashboard/dashboard/dashboard.html`
- `frontend/src/app/features/dashboard/dashboard/dashboard.ts`

---

## Phase 5: Advanced Features (5 tasks)

### 🟣 Task 21: Create Backend Statistics Aggregation Endpoints
**Priority:** Medium  
**Estimated Time:** 3 hours

- [ ] Create `/api/reptiles/stats` endpoint returning:
  - Total reptiles
  - Active count
  - Species distribution
  - Average age
- [ ] Create `/api/reptiles/{id}/analytics` endpoint:
  - Weight statistics (min, max, avg, trend)
  - Feeding frequency analysis
  - Shedding cycle analysis
  - Growth rate calculations
- [ ] Implement caching for expensive calculations
- [ ] Add date range parameters

**Files to create/modify:**
- `backend/src/main/java/com/reptilemanagement/rest/controller/ReptileController.java`
- `backend/src/main/java/com/reptilemanagement/rest/service/ReptileService.java`

---

### 🟣 Task 22: Implement Date Range Filters for All Logs
**Priority:** Medium  
**Estimated Time:** 2 hours

- [ ] Add date range picker component
- [ ] Implement filtering on:
  - Feeding logs
  - Weight logs
  - Shedding logs
  - Poop logs
- [ ] Add preset ranges (Last 7 days, Last 30 days, Last 3 months, All time)
- [ ] Update charts to respect date filter
- [ ] Save filter preference in local storage

**Files to modify:**
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.html`
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.ts`

---

### 🟣 Task 23: Add Export Functionality (CSV/PDF) for Logs
**Priority:** Low  
**Estimated Time:** 3 hours

- [ ] Install PDF generation library: `pnpm add jspdf jspdf-autotable`
- [ ] Create export service
- [ ] Implement CSV export:
  - All logs for a reptile
  - Individual log types
  - Date-filtered logs
- [ ] Implement PDF export:
  - Formatted report with charts
  - Include statistics and summary
- [ ] Add export buttons to log views

**Files to create:**
- `frontend/src/app/core/services/export.service.ts`

**Files to modify:**
- `frontend/src/app/features/reptile-management/reptile-detail/reptile-detail.component.html`

---

### 🟣 Task 24: Create Notification System for Feeding Reminders
**Priority:** Low  
**Estimated Time:** 4 hours

- [ ] Design notification system architecture
- [ ] Create backend notification service
- [ ] Implement scheduled jobs (check feeding due dates)
- [ ] Add email notification support (optional)
- [ ] Create in-app notification component
- [ ] Add notification preferences per reptile
- [ ] Allow users to set custom feeding schedules

**Files to create:**
- `backend/src/main/java/com/reptilemanagement/rest/service/NotificationService.java`
- `frontend/src/app/shared/components/notifications/notifications.component.ts`

---

### 🟣 Task 25: Add Data Validation and Error Handling for All Forms
**Priority:** High  
**Estimated Time:** 3 hours

- [ ] Review all forms for proper validation
- [ ] Add frontend validation:
  - Required fields
  - Date validation (not in future)
  - Numeric ranges (weight > 0, etc.)
  - String length limits
- [ ] Add backend validation with proper error messages
- [ ] Implement consistent error display
- [ ] Add form submission loading states
- [ ] Handle network errors gracefully
- [ ] Add retry mechanisms for failed requests

**Files to modify:**
- All form components
- Backend DTOs with validation annotations
- Error interceptor

---

## 📈 Progress Tracking

### Completion Status by Phase
- **Phase 1:** 5/5 tasks complete (100%) - DONE
- **Phase 2:** 0/4 tasks complete (0%)
- **Phase 3:** 0/5 tasks complete (0%)
- **Phase 4:** 0/6 tasks complete (0%)
- **Phase 5:** 0/5 tasks complete (0%)

**Overall Progress:** 5/25 tasks complete (20%)

---

## 🎯 Next Steps

### Immediate Priorities (Week 1-2)
1. Install Chart.js (Task 6)
2. Complete poop log backend (Tasks 2-3)
3. Add poop log frontend model (Tasks 4-5)
4. Create tabbed logs interface (Task 10)

### Short-term Goals (Week 3-4)
1. Implement weight tracking charts (Task 7)
2. Complete feeding log UI (Task 11)
3. Complete weight log UI (Task 12)
4. Start dashboard overhaul (Task 15)

### Long-term Goals (Month 2+)
1. Complete all log types (Tasks 13-14)
2. Finish dashboard widgets (Tasks 16-20)
3. Add advanced features (Tasks 21-25)

---

## 📝 Notes

### Technical Decisions
- **Chart Library:** Chart.js chosen for its simplicity and Angular compatibility
- **Date Handling:** date-fns for lightweight date manipulation
- **Export:** jsPDF for PDF generation, native CSV support

### Considerations
- Keep mobile responsiveness in mind for all UI changes
- Maintain consistent design with DaisyUI theme
- Ensure all new endpoints follow REST conventions
- Add proper TypeScript typing for all models
- Write unit tests for critical business logic

### Future Enhancements (Beyond Current Scope)
- Mobile app (React Native or Flutter)
- Breeding tracker module
- Multi-language support (i18n)
- Dark/light theme toggle
- Advanced analytics and reporting
- Integration with veterinary records
- QR code generation for enclosure tags
- Automated feeding reminders via email/SMS
- Social features (share collection with friends)

---

## 🤝 Contributing

When working on tasks:
1. Create a feature branch: `git checkout -b feature/task-{number}-{description}`
2. Mark task as in progress in this file
3. Commit regularly with clear messages
4. Update task status upon completion
5. Test thoroughly before marking complete
6. Create pull request for review

---

**Last Review:** January 16, 2026  
**Next Review:** TBD
