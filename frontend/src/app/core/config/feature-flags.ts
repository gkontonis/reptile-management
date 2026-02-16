// Feature configuration - easily enable/disable features for different deployments
export const FEATURE_FLAGS = {
  // Disabled - not used in reptile management
  todos: false,

  // Disabled - not used in reptile management
  userManagement: true,

  // Core reptile management functionality
  reptileManagement: true,
} as const;

export type FeatureFlag = keyof typeof FEATURE_FLAGS;
