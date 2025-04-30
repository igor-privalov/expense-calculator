export interface Expense {
    id: string;
    category: string;
    amount: number;
    date: string;
}

export interface ExpenseStats {
    total: number;
    averageDaily: number;
    topExpenses: Expense[];
}

export interface ExpenseFormData {
    category: string;
    amount: number;
} 